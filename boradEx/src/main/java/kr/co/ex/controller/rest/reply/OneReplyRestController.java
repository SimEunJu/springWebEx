package kr.co.ex.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.service.ReplyService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController
@RequiredArgsConstructor
@Log4j
@RequestMapping("/board/api/reply")
public class ReplyRestController {
	
	@NonNull private ReplyService replyServ;
	
	@GetMapping(value="", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<ReplyVO>> getReplies(Criteria cri){
		List<ReplyVO> replies = null;
		try {
			replies = replyServ.listReplyByWriter(cri);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(replies, HttpStatus.OK);
	}
	
	@DeleteMapping(value="", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<ReplyVO>> deleteReplies(@RequestBody List<Integer> rno){
		List<ReplyVO> replies = null;
		try {
			replyServ.removeReplies(rno);
			replies = replyServ.listReplyByWriter(new Criteria());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(replies,HttpStatus.OK);
	}
}
