package kr.co.ex.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.service.ReplyService;

@RestController
@RequestMapping("/replies")
public class ReplyController {

	@Autowired
	ReplyService serv;
	
	@PostMapping("")
	public ResponseEntity<String> register(@RequestBody ReplyVO vo){
		try {
			System.out.println(vo.toString());
			serv.addReply(vo);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{bno}/{page}")
	public ResponseEntity<Map<String, Object>> list(@PathVariable Integer bno, 
			@PathVariable Integer page){
		try {
			Criteria cri = new Criteria();
			cri.setPage(page);
			
			PageMaker pm = new PageMaker();
			pm.setCri(cri);
			pm.setTotalCount(serv.getTotalCount(bno));
			
			Map<String, Object> map = new HashMap<>();
			map.put("paging", pm);
			map.put("replies", serv.listCriteriaReply(bno, cri));
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/{rno}")
	public ResponseEntity<String> update(@PathVariable Integer rno, @RequestBody ReplyVO vo){
		try {
			serv.modifyReply(vo);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{rno}")
	public ResponseEntity<String> remove(@PathVariable Integer rno, ReplyVO vo){
		try {
			serv.removeReply(rno);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
