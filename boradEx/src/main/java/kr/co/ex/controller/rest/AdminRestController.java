package kr.co.ex.controller.rest;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.MemberVO;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.exception.UndefinedMemberType;
import kr.co.ex.service.AdminStatService;
import kr.co.ex.service.MemberService;
import kr.co.ex.service.MsgService;
import kr.co.ex.util.PaginationUtils;
import kr.co.ex.util.UserType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/board/api/admin")
public class AdminRestController {

	@NonNull private AdminStatService statServ;
	@NonNull private MemberService memServ;
	@NonNull private MsgService msgServ;

	@GetMapping(value = "/inout", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<Map<String, List<Integer>>> listInout(@RequestParam String type) {
		
		Map<String, List<Integer>> res = new HashMap<>();
		res.put("leave", statServ.getUserLeaveCount(type));
		res.put("join", statServ.getUserJoinCount(type));
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping(value = "/visit", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<Map<String, List<Integer>>> listVisit(@RequestParam String type) {
		
		Map<String, List<Integer>> res = new HashMap<>();
		res.put("visit", statServ.getVisitCount(type));
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping(value = "/board", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<Map<String, List<Integer>>> listBoard(@RequestParam String type) {
		
		Map<String, List<Integer>> res = new HashMap<>();
		res.put("board", statServ.getPostCount(type));
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping(value = "/user", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<Map<String, Object>> listCriMember(@RequestParam(defaultValue = "all") String type, Criteria cri) {
		
		UserType userType = getMemberType(type);
		Map<String, Object> ret = new HashMap<>();
		
		PageMaker pm = PaginationUtils.pagination(cri, memServ.getMemberCnt());
		
		ret.put("users", memServ.ListCategorizedMember(userType, cri));
		ret.put("pagination", pm);
		
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	
	private UserType getMemberType(String type){
		switch (type) {
		case "all-type":		
			return UserType.ALL;
		case "active":	
			return UserType.ACTIVE;
		case "report":	
			return UserType.REPORT;
		case "banned":
			return UserType.BANNED;
		case "sleep":
			return UserType.SLEEP;
		case "leave":
			return UserType.LEAVE;
		default:
			throw new UndefinedMemberType(type);
		}
	}
	
	@PostMapping(value = "/usertype", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Map<String, List<MemberVO>>> changeUserType(@RequestParam String type, @RequestParam String showType, @RequestBody List<String> members) {
		
		UserType prevUserType = getMemberType(type);
		memServ.updateState(members, prevUserType);
		
		UserType nextUserType = getMemberType(showType);
		Map<String, List<MemberVO>> list = Collections.singletonMap("users", memServ.ListCategorizedMember(nextUserType, new Criteria()));
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping(value = "/user/find", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<Map<String, List<MemberVO>>> findMember(@RequestParam String keyword) {
		
		Map<String, List<MemberVO>> list = Collections.singletonMap("users", memServ.getMemberByKeyword(keyword));
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
}
