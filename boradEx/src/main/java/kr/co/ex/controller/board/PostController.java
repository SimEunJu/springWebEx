package kr.co.ex.controller.board;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.UserLikeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/daily/{boardNo}")
public class PostController {
	
	@NonNull private BoardService boardServ;
	@NonNull private UserLikeService likeServ;
	@NonNull private NotificationService notiServ;
	
	// 검색결과 유지
	@GetMapping("")
	public String showPost(@PathVariable int boardNo, @RequestParam(required = false) String from,
			@RequestParam(required = false) int nno, @ModelAttribute("cri") SearchCriteria cri, Model model) {
		try {
			boardServ.updateViewCnt(boardNo);
			if (from != null && from.equals("noti"))
				notiServ.markReadFlagNotification(nno);
			
			// 사용자 알림에서 넘어 온 경우 해당 댓글 하이라이트
			//model.addAttribute("hilightedRno", notiServ.getRno());

			model.addAttribute("isUserLiked", likeServ.isUserLiked(boardNo));
			model.addAttribute("replyCnt", boardServ.getReplyCnt(boardNo));

			BoardVO board = boardServ.read(boardNo);
			model.addAttribute("board", board);
			model.addAttribute("specificTitle", board.getTitle());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "board/post.part";
	}
	
	// 검색어 유지
	@PostMapping("/mod")
	public String modifyPost(BoardVO board, SearchCriteria cri, RedirectAttributes attrs) {
		try {
			boardServ.modify(board);
			attrs.addFlashAttribute("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/daily" + cri.makeSearch();
	}

	// 검색어 유지
	// interceptor에서 권한 체크
	@GetMapping("/mod")
	public String showPostModification(@PathVariable int boardNo, @ModelAttribute("cri") SearchCriteria cri, Model model) {
		try {
			BoardVO board = boardServ.read(boardNo);
			model.addAttribute("board", board);
			
			// <title> 태그에 사용
			model.addAttribute("specificTitle", board.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "board/modify.part";
	}

	@GetMapping("/temp")
	@PreAuthorize("hasRole('USER')")
	public String showDeletedPost(@PathVariable int boardNo, HttpServletResponse res, Model model) {
		BoardVO vo = null;
		try {
			vo = boardServ.read(boardNo);
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			if (!username.equals(vo.getWriter())) {
				res.setStatus(HttpStatus.UNAUTHORIZED.value());
				return "redirect:/board/user";
			}
			model.addAttribute("board", vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "temp/post.part";
	}
}
