package kr.co.ex.controller.rest.board;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
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
import kr.co.ex.service.ReplyService;
import kr.co.ex.util.file.DeleteFileUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board/{boardNo}")
public class PostRestController {
	
	@NonNull private BoardService boardServ;
	@NonNull private ReplyService replyServ;
	
	// 익명 회원이 비밀번호 확인 후 게시글 수정
	@PostMapping("/mod")
	public ResponseEntity<Void> modifyAnonyPost(@PathVariable int boardNo, @RequestParam String password,
			SearchCriteria cri) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boolean isAnonymous = auth.getAuthorities()
				.stream().anyMatch(p -> "ROLE_ANONYMOUS".equals(p.getAuthority()));
			if(!isAnonymous){
				String writer = boardServ.getWriterName(boardNo);
				log.info(writer);
				log.info(auth.getName());
				if("관리자".equals(writer)) return new ResponseEntity<>(HttpStatus.OK);
				else if(auth.getName().equals(writer)) return new ResponseEntity<>(HttpStatus.OK);
				//관리자 userinfo 정리 필요
				else throw new AccessDeniedException("게시글 작성자와 변경 시도하려는 사용자가 다릅니다.");
			}
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
	public ResponseEntity<Void> deleteAnonyPost(@PathVariable int boardNo, BoardVO vo, SearchCriteria cri) {
		try {
			boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
					.stream().map(a -> a.getAuthority()).anyMatch(a -> "ROLE_ADMIN".equals(a));
			// 게시글 삭제
			// 공지글 삭제
			log.info(vo.toString());
			if(isAdmin) boardServ.removeNoti(boardNo);
			else {
				vo.setBno(boardNo);
				boardServ.remove(vo);
			}
			// 게시글에 달린 댓글 삭제
			replyServ.removeRepliesByPost(boardNo);
			// 게시글에 첨부된 파일 삭제
			
			List<AttachVO> attaches = boardServ.getAttach(boardNo);
			DeleteFileUtils.deleteFiles(attaches);

		} catch (AccessDeniedException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
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
	public ResponseEntity<Void> updatePostLike(@PathVariable int boardNo, @RequestParam("diff") int likeCnt){
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
	public ResponseEntity<Void> updateReportCnt(@PathVariable int boardNo, @RequestParam int diff){
		try{
			boardServ.updateReportCnt(boardNo, diff);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
