package kr.co.ex.controller.board;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.ex.common.NoticeBoardControl;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.exception.UndefinedBoardTypeException;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.UserLikeService;
import kr.co.ex.util.PaginationUtils;
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
	
	// 이미지 파일이 저장되는 루트 경로 
	// servlet-context.xml에서 설정
	@Resource String uploadPath;

	// [0, 100) 공지사항 전용 번호 범위
	private List<BoardVO> getNoticeBoard(SearchCriteria searchCri) throws Exception{
		NoticeBoardControl.NoticeBoardCriteria notiCri = notiControl.getNoticeBoardCri(searchCri);
		return boardServ.listNotice(notiCri);
	}

	// 검색결과 유지
	@GetMapping("")
	public String showPostList(@ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			log.info(cri.toString());
			
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
				// 공지글만 검색하고 싶을 때 xml에서 search 가능하도록 변경 또는 추가해야
				// 공지글 페이징 하고 싶을 때 totalCnt 추가해야
				boardList = getNoticeBoard(cri);
				break;
			default:
				throw new UndefinedBoardTypeException(cri.getType().toString());
			}
			
			PageMaker pagination = PaginationUtils.pagination(cri, totalCount);
			
			model.addAttribute("pagination", pagination);
			model.addAttribute("boardList", boardList);
			
			model.addAttribute("noticeList", getNoticeBoard(cri));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "board/daily.part";
	}
	
	@GetMapping("/new")
	public String register(){
		return "board/new.part";
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
	
	@GetMapping("/notice/new")
	@PreAuthorize("hasRole('ADMIN')")
	public String registerNotice(){
		return "dashBoard/admin/new.part";
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
	
	
}
