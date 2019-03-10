package kr.co.ex.controller.rest;


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
import lombok.extern.log4j.Log4j;

@RestController
@Log4j
@RequiredArgsConstructor
@RequestMapping("/board/api/admin")
public class AdminRestController {

	@NonNull
	private AdminStatService statServ;
	@NonNull
	private MemberService memServ;
	@NonNull
	private MsgService msgServ;

	@GetMapping(value = "/inout", produces = "application/json; charset=UTF-8")
	public ResponseEntity<Map<String, List<Long>>> listInout(@RequestParam String type) {
		Map<String, List<Long>> res = new HashMap<>();
		res.put("leave", statServ.getUserLeaveCount(type));
		res.put("join", statServ.getUserJoinCount(type));
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping(value = "/visit", produces = "application/json; charset=UTF-8")
	public ResponseEntity<Map<String, List<Long>>> listVisit(@RequestParam char type) {
		Map<String, List<Long>> res = new HashMap<>();

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping(value = "/board", produces = "application/json; charset=UTF-8")
	public ResponseEntity<Map<String, List<Long>>> listBoard(@RequestParam String type) {
		Map<String, List<Long>> res = new HashMap<>();
		res.put("board", statServ.getPostCount(type));
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping(value = "/user", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<Map<String, Object>> listCriMember(@RequestParam(required = false) String move,
			@RequestParam(defaultValue = "all") String type, Criteria cri) {
		
		UserType userType = getMemberType(type);
		Map<String, Object> ret = new HashMap<>();
		
		PageMaker pm = null;
		if (move != null)
			pm = PaginationUtils.pagination(move, cri, memServ.getMemberCnt());
		log.info(memServ.ListCategorizedMember(userType, cri));
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
	public ResponseEntity<List<MemberVO>> changeUserType(@RequestParam String type, @RequestParam String showType, @RequestBody List<String> members) {
		
		log.info(members);
		UserType userType = getMemberType(type);
		memServ.updateState(members, userType.getTypeInitial());
		
		UserType showedType = getMemberType(showType);
		
		return new ResponseEntity<>(memServ.ListCategorizedMember(showedType, new Criteria()),HttpStatus.OK);
	}
	
	@GetMapping(value = "/user/find", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<Map<String, List<MemberVO>>> findMember(@RequestParam String keyword) {
		Map<String, List<MemberVO>> ret = new HashMap<>();
		ret.put("users", memServ.getMemberByKeyword(keyword));
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
}
