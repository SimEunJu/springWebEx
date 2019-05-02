package kr.co.ex.controller.rest.reply;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.dto.ReplyDto;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.ReplyService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/reply/{boardNo}/{rno}")
public class OneReplyRestController {
	
	@NonNull private ReplyService replyServ;
	@NonNull private BoardService boardServ;
	@NonNull private NotificationService notiServ;
	
	@GetMapping(value="/added", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<ReplyDto>> addedList(@PathVariable int boardNo, @PathVariable("rno") int parRno, Criteria cri){
		try{
			List<ReplyDto> replies = replyServ.listCriteriaAddedReply(boardNo, parRno, cri);
			return new ResponseEntity<>(replies, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value="/report", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> reportReply(@PathVariable int rno, @RequestBody ReplyVO vo){
		try {
			replyServ.reportReply(rno);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// interceptor에서 인증
	@DeleteMapping(value="", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> removeReply(@PathVariable int boardNo, @PathVariable int rno){
		String deleteType = null;
		try {
			deleteType = replyServ.removeReply(rno, boardNo);
			return new ResponseEntity<>(deleteType, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="/pw", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> matchPasswordAndDelete(@PathVariable int boardNo, @PathVariable int rno,
			@RequestParam String pw){
		String deleteType = null;
		try {
			deleteType = replyServ.removeAnonymousReply(rno, boardNo, pw);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(AccessDeniedException e){
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(deleteType, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
