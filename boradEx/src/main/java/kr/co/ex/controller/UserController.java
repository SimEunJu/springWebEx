package kr.co.ex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.ex.service.MemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequiredArgsConstructor
@RequestMapping("/board/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

	@NonNull
	private MemberService memServ;
	
	@PostMapping("/report")
	public ResponseEntity<String> reportUser(@RequestParam String username, @RequestParam int diff){
		try{
			// 존재하지 않은 사용자 이름을 악의적으로 전송할 수 있음
			memServ.updateReportCnt(username, diff);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	
	@GetMapping("")
	public String showUserInfo(){
		
		return "board/user";
	}

	@GetMapping("/info/auth")
	public String showUserAuthInfo(){
		
		return "board/user/auth";
	}

}
