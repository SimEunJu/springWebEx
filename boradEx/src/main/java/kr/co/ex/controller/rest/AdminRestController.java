package kr.co.ex.controller.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.BoardVO;
import kr.co.ex.service.AdminStatService;
import kr.co.ex.service.BoardService;
import kr.co.ex.util.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController
@Log4j
@RequiredArgsConstructor
@RequestMapping("/board/api/admin/stat")
public class AdminRestController {

	@NonNull
	private AdminStatService statServ;

	
	@GetMapping(value="/inout", produces="application/json; charset=UTF-8")
	public ResponseEntity<Map<String,List<Long>>> listInout(@RequestParam char type){
		Map<String, List<Long>> res = new HashMap<>();
		res.put("leave", statServ.getUserLeaveCount(type));
		res.put("join", statServ.getUserJoinCount(type));
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping(value="/visit", produces="application/json; charset=UTF-8")
	public ResponseEntity<Map<String,List<Long>>> listVisit(@RequestParam char type){
		Map<String, List<Long>> res = new HashMap<>();
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping(value="/inout", produces="application/json; charset=UTF-8")
	public ResponseEntity<Map<String,List<Long>>> listBoard(@RequestParam char type){
		Map<String, List<Long>> res = new HashMap<>();
		res.put("board", statServ.getPostCount(type));
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
