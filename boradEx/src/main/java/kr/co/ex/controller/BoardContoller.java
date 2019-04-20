package kr.co.ex.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.ex.common.NoticeBoardControl;
import kr.co.ex.controller.utils.CookieMaker;
import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.exception.UndefinedBoardTypeException;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.UserLikeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/daily")
public class BoardContoller {
	
	@NonNull private BoardService boardServ;
	@NonNull private UserLikeService likeServ;
	@NonNull private NotificationService notiServ;
	@NonNull private NoticeBoardControl notiControl;
	
	// �̹��� ������ ����Ǵ� ��Ʈ ���		
	@Resource
	String uploadPath;

	// [20, 50) �������� ���� ���� ��ȣ���Դϴ�.
	private List<BoardVO> getNoticeBoard(SearchCriteria searchCri) throws Exception{
		NoticeBoardControl.NoticeBoardCriteria notiCri = notiControl.getNoticeBoardCri(searchCri);
		return boardServ.listNotice(notiCri);
	}
	
	/*
	* @param cri �� ��° page����, page���� �� ���� ���� �����ִ���, �˻� Ÿ��, �˻� Ű����
	*/

	// �˻���� ����
	@GetMapping("")
	public String showPostList(@ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			log.info(cri.toString());
			PageMaker pageMaker = new PageMaker();
			
			int totalCount = 0;
			List<BoardVO> boardList = null;
			
			switch (cri.getType()) {
			case ALL:
				totalCount = boardServ.getSearchCnt(cri);
				boardList = boardServ.listSearch(cri);
				break;
			case HOT:
				totalCount = boardServ.getSearchCnt(cri);
				boardList = boardServ.listSearch(cri);
				break;
			case NOTICE:
				// �����۸� �˻��ϰ� ���� �� xml���� search �����ϵ��� ���� �Ǵ� �߰��ؾ�
				// ������ ����¡ �ϰ� ���� �� totalCnt �߰��ؾ�
				boardList = getNoticeBoard(cri);
				break;
			default:
				throw new UndefinedBoardTypeException(cri.getType().toString());
			}

			pageMaker.setCri(cri);
			pageMaker.setTotalCount(totalCount);
			
			model.addAttribute("pagination", pageMaker);
			model.addAttribute("boardList", boardList);
			
			model.addAttribute("noticeList", getNoticeBoard(cri));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "board/daily.part";
	}
	
	@GetMapping("/{boardNo}/like")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Void> updatePostLike(@PathVariable int boardNo, @RequestParam int likeCnt){
		try {			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boardServ.updateLike(boardNo, likeCnt, auth.getName());
			
		} catch (Exception e) {
			new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/{boardNo}/report")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> updateReportCnt(@PathVariable int boardNo, @RequestParam int diff){
		try{
			log.info(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
			boardServ.updateReportCnt(boardNo, diff);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/new")
	public String register(){
		return "board/new.part";
	}
	
	@GetMapping("/notice/new")
	@PreAuthorize("hasRole('ADMIN')")
	public String registerNotice(){
		return "dashBoard/admin/new.part";
	}
	
	@PostMapping("/new")
	public String registerPost(BoardVO board, RedirectAttributes attrs){
		try {
			boardServ.register(board);
			attrs.addFlashAttribute("msg", "success");
		
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/daily";
	}
	
	@PostMapping("/notice/new")
	@PreAuthorize("hasRole('ADMIN')")
	public String registerNoticePost(@Valid BoardVO board, RedirectAttributes attrs){
		try {
			boardServ.registerNotice(board);
			attrs.addFlashAttribute("msg", "success");
		
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/daily";
	}
	
	// �˻���� ����
	@GetMapping("/{boardNo}")
	public String showEachPost(@CookieValue(value="notiPoll", required=false) Cookie notiCk, 
			@PathVariable int boardNo, @RequestParam(required=false) String from, 
			@RequestParam(required=false) Integer nno, @ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			boardServ.updateViewCnt(boardNo);
			if(from != null && from.equals("noti")) notiServ.markReadFlagNotification(nno);
			
			model.addAttribute("isUserLiked", likeServ.isUserLiked(boardNo));
			model.addAttribute("replyCnt", boardServ.getReplyCnt(boardNo));
			
			BoardVO board =  boardServ.read(boardNo);
			model.addAttribute("board", board);
			model.addAttribute("specificTitle", board.getTitle());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "board/post.part";
	}
	
	@GetMapping(value="/{boardNo}/attach", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<AttachVO> addAttachToPost(@PathVariable int boardNo) throws Exception{
		List<AttachVO> attaches = boardServ.getAttach(boardNo);
		return attaches;
	}
	
	// �˻��� ����
	// interceptor���� ���� üũ
	@GetMapping("/{boardNo}/mod")
	public String modifyPost(@PathVariable int boardNo, @ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			BoardVO board = boardServ.read(boardNo);
			model.addAttribute("board", board);
			model.addAttribute("specificTitle", board.getTitle());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "board/modify.part";
	}
	
	// �˻��� ����
	@PostMapping("/{boardNo}")
	//@PreAuthorize("isAuthenticated()")
	public String modifyPost(BoardVO board, SearchCriteria cri, RedirectAttributes attrs){
		try {
			boardServ.modify(board);
			attrs.addFlashAttribute("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/daily"+cri.makeSearch();
	}
	
	@GetMapping("/{boardNo}/temp")
	@PreAuthorize("hasRole('USER')")
	public String showDeletedPost(@PathVariable int boardNo, HttpServletRequest req,
			HttpServletResponse res, Model model){
		BoardVO vo = null;
		try {
			vo = boardServ.read(boardNo);
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			if(!username.equals(vo.getWriter())){
				return "redirect:/board/user";
			}
			model.addAttribute("board", vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "temp/post.part";
	}
}
