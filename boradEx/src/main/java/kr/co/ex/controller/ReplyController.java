package kr.co.ex.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

	@NonNull private ReplyService replyServ;
	@NonNull private BoardService boardServ;
	@NonNull private NotificationService notiServ;
	
	@PostMapping(value="", consumes="application/json")
	public ResponseEntity<String> registerReply(@PathVariable int boardNo, @RequestBody ReplyVO vo){
		try {
			log.info(vo.toString());
			replyServ.addReply(vo);
			
			String writer = boardServ.getWriterName(boardNo);
			if(writer.equals(vo.getReplyer())){
				NotificationVO noti = NotificationVO.builder()
						.rno(vo.getRno())
						.bno(boardNo)
						.username(writer)
						.build();
				notiServ.registerNotification(noti);
			}
			return new ResponseEntity<String>("success", HttpStatus.OK);
		
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
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
		
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value="/added/{parRno}/{page}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<ReplyDto>> addedList(@PathVariable int bno, @PathVariable int parRno, @PathVariable int page){
		try{
			Criteria cri = new Criteria();
			cri.setPage(page);
			List<ReplyDto> replies = replyServ.listCriteriaAddedReply(bno, parRno, cri);
			
			return new ResponseEntity<>(replies, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value="/report/{rno}", produces={MediaType.TEXT_PLAIN_VALUE})
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> reportReply(@PathVariable int rno, ReplyVO vo){
		try {
			replyServ.reportReply(rno);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping(value="/{rno}", produces={MediaType.TEXT_PLAIN_VALUE})
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> remove(@PathVariable int bno, @PathVariable int rno, ReplyVO vo){
		try {
			replyServ.removeReply(rno, bno);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
