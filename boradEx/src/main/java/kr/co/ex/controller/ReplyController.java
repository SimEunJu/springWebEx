package kr.co.ex.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.ReplyService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController
@Log4j
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

	@NonNull
	private ReplyService serv;
	
	@NonNull
	private NotificationService notiServ;
	
	@PostMapping(consumes="application/json", produces={MediaType.TEXT_PLAIN_VALUE})
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> registerReply(@RequestBody ReplyVO vo){
		try {
			log.info(vo.toString());
			serv.addReply(vo);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/{bno}/{page}", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	@PreAuthorize("permitAll()")
	public ResponseEntity<Map<String, Object>> getReplyList(
			@PathVariable Integer bno, @PathVariable Integer page, HttpServletRequest req){
		try {
			log.info("reply");
			Criteria cri = new Criteria();
			cri.setPage(page);
			
			PageMaker pm = new PageMaker();
			pm.setCri(cri);
			pm.setTotalCount(serv.getTotalCount(bno));
			
			Map<String, Object> map = new HashMap<>();
			map.put("pageMaker", pm);
			String currentUser = null;
			if(req.getUserPrincipal() != null) currentUser = req.getUserPrincipal().getName();
			
			map.put("replies", serv.listCriteriaReply(bno, cri, currentUser));
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value="/added/{parRno}/{page}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<ReplyVO>> addedList(@PathVariable int parRno, @PathVariable int page, HttpServletRequest req){
		try{
			Criteria cri = new Criteria();
			cri.setPage(page);
			
			String currentUser = null;
			if(req.getUserPrincipal() != null) currentUser = req.getUserPrincipal().getName();
			
			List<ReplyVO> replies = serv.listCriteriaAddedReply(parRno, cri);
			// 과연 여기에 두는 것이 최선인가...
			NotificationVO noti = NotificationVO
				.builder()
					.userid(serv.getReplyer(parRno))
					.rno(parRno)
					.bno(null)
					.build();
			notiServ.registerNotification(noti);
			return new ResponseEntity<>(replies, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value="/{rno}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ReplyVO> getReply(@PathVariable int rno){
		ResponseEntity<ReplyVO> res = null;
		try {
			res = new ResponseEntity<>(serv.getReply(rno), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
 	
	@PutMapping(value="/{rno}", produces={MediaType.TEXT_PLAIN_VALUE})
	@PreAuthorize("principal.username == #vo.replyer")
	public ResponseEntity<String> update(@PathVariable Integer rno, ReplyVO vo){
		try {
			serv.modifyReply(vo);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping(value="/{rno}", produces={MediaType.TEXT_PLAIN_VALUE})
	@PreAuthorize("principal.username == #vo.replyer")
	public ResponseEntity<String> remove(@PathVariable Integer rno, @RequestParam String name, ReplyVO vo){
		try {
			serv.removeReply(rno);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
