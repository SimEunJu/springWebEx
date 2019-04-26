package kr.co.ex.controller.rest.board;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.service.BoardService;
import kr.co.ex.util.file.DeleteFileUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board/{boardNo}")
public class PostRestController {
	
	@NonNull private BoardService boardServ;
	
	// 익명 회원이 비밀번호 확인 후 게시글 수정
	@PostMapping("/mod")
	public ResponseEntity<Void> modifyAnonyPost(@PathVariable int boardNo, @RequestParam String password,
			SearchCriteria cri) {
		try {
			if (boardServ.matchPassword(boardNo, password)) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else
				throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");

		} catch (AccessDeniedException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 익명 회원이 비밀번호 확인 후 삭제
	@PostMapping("/rem")
	public ResponseEntity<Void> deleteAnonyPost(BoardVO vo, SearchCriteria cri) {
		try {
			// 게시글 삭제
			boardServ.remove(vo);
			// 게시글에 달린 댓글 삭제
			// replyServ.removeByBno(vo.getBno());
			// 게시글에 첨부된 파일 삭제
			DeleteFileUtils.deleteFiles(boardServ.getAttach(vo.getBno()));

		} catch (AccessDeniedException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(value="/attach", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<AttachVO> addAttachToPost(@PathVariable int boardNo, HttpServletResponse res) throws Exception{
		List<AttachVO> attaches = null;
		try{
			attaches = boardServ.getAttach(boardNo);
			res.setStatus(HttpStatus.CREATED.value());
		}catch(Exception e){
			e.printStackTrace();
			res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return attaches;
	}
	
	@GetMapping("/like")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Void> updatePostLike(@PathVariable int boardNo, @RequestParam int likeCnt){
		try {			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boardServ.updateLike(boardNo, likeCnt, auth.getName());
			
		} catch (Exception e) {
			new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/report")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> updateReportCnt(@PathVariable int boardNo, @RequestParam int diff){
		try{
			boardServ.updateReportCnt(boardNo, diff);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
}
