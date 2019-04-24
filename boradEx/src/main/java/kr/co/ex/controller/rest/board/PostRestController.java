package kr.co.ex.controller.rest.board;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import kr.co.ex.service.BoardService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board/{boardNo}")
public class PostRestController {
	
	@NonNull private BoardService boardServ;
	
	@GetMapping(value="/attach", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<AttachVO> addAttachToPost(@PathVariable int boardNo) throws Exception{
		List<AttachVO> attaches = boardServ.getAttach(boardNo);
		return attaches;
	}
	
	@GetMapping("/like")
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
	
	@PostMapping("/report")
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
}
