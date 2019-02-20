package kr.co.ex.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.ex.service.AdminStatService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequiredArgsConstructor
public class UserController {

	@NonNull
	private AdminStatService statServ;
	
	@GetMapping("/board/user")
	@PreAuthorize("isAuthenticated()")
	public String showUserInfo(){
		
		return "board/user";
	}
	
	@GetMapping("/board/admin")
	//@PreAuthorize("isAuthenticated()")
	public String showAdminInfo(Model model){
		model.addAttribute("postCnt", statServ.getPostCount('d'));
		model.addAttribute("postLeaveCnt", statServ.getUserLeaveCount('d'));
		model.addAttribute("postJoinCnt", statServ.getUserJoinCount('d'));
		return "/dashBoard/admin/adminMain";
	}
	
	@GetMapping("/board/user/info/auth")
	@PreAuthorize("isAuthenticated()")
	public String showUserAuthInfo(){
		
		return "board/user/auth";
	}

}
