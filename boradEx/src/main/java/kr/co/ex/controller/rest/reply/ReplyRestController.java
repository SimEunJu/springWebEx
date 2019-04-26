package kr.co.ex.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.NotificationVO;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.dto.ReplyDto;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.ReplyService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/board/daily/{boardNo}/reply")
@RequiredArgsConstructor
public class ReplyController {

	@NonNull private BoardService boardServ;
	@NonNull private ReplyService replyServ;
	@NonNull private NotificationService notiServ;
	
	@PostMapping(value="", consumes="application/json")
	public ResponseEntity<String> registerReply(@PathVariable int boardNo, @RequestBody ReplyVO vo){
		try {
			replyServ.addReply(vo);
			
			String writer = boardServ.getWriterName(boardNo);
			// ´ñ±ÛÀÌ ´Þ¸®´Â °æ¿ì
			notiServ.registerNotification(vo, boardNo);
			return new ResponseEntity<>("success", HttpStatus.OK);
		
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/{page}", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<Map<String, Object>> getReplyList(@PathVariable int boardNo, @PathVariable int page){
		try {
		
			Criteria cri = new Criteria();
			cri.setPage(page);
			
			PageMaker pm = new PageMaker();
			pm.setCri(cri);
			// ´ë´ñ±Û °¹¼ö´Â Á¦¿Ü
			pm.setTotalCount(replyServ.getTotalCount(boardNo, true));
			
			Map<String, Object> map = new HashMap<>();
			map.put("pagination", pm);
		
			map.put("replies", replyServ.listCriteriaReply(boardNo, cri));
		
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value="/added/{parRno}/{page}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<ReplyDto>> addedList(@PathVariable int boardNo, @PathVariable int parRno, @PathVariable int page){
		try{
			Criteria cri = new Criteria();
			cri.setPage(page);
			List<ReplyDto> replies = replyServ.listCriteriaAddedReply(boardNo, parRno, cri);
			
			return new ResponseEntity<>(replies, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/report/{rno}", produces={MediaType.TEXT_PLAIN_VALUE})
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> reportReply(@PathVariable int rno, ReplyVO vo){
		try {
			replyServ.reportReply(rno);
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value="/{rno}", produces={MediaType.TEXT_PLAIN_VALUE})
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> update(@PathVariable int rno, ReplyVO vo){
		try {
			replyServ.modifyReply(vo);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping(value="/{rno}", produces={MediaType.TEXT_PLAIN_VALUE})
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> remove(@PathVariable int boardNo, @PathVariable int rno){
		String deleteType = null;
		try {
			deleteType = replyServ.removeReply(rno, boardNo);
			return new ResponseEntity<>(deleteType, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/{rno}/pw", produces={MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> matchPasswordAndDelete(@PathVariable int boardNo, @PathVariable int rno,
			@RequestParam String pw){
		String deleteType = null;
		try {
			deleteType = replyServ.removeAnonymousReply(rno, boardNo, pw);
			return new ResponseEntity<>("success", HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			if(e instanceof AccessDeniedException){ 
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<>(deleteType, HttpStatus.BAD_REQUEST);
		}
	}
}
