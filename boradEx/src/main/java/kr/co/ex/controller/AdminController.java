package kr.co.ex.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board/admin")
@PreAuthorize("hasRole(ADMIN)")
public class AdminController {
	
	@GetMapping("/noti")
	public String showNotification(){
		return "/board/noti";
	}
	@GetMapping("/msg")
	public String showMsg(){
		return "/board/msg";
	}
	@GetMapping("/user")
	public String manageUser(){
		return "/board/admin/userInfo";
	}
	@GetMapping("/post")
	public String managePost(){
		return "/board/admin/post";
	}
	@GetMapping("/reply")
	public String manageReply(){
		return "/board/admin/reply";
	}
	@GetMapping("/info")
	public String showAdminInfo(){
		return "/board/admin/adminInfo";
	}
	
	
}
