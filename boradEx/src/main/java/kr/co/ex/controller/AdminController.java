package kr.co.ex.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.ex.service.AdminStatService;
import kr.co.ex.service.MsgService;
import kr.co.ex.service.NotificationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board/admin")
//@PreAuthorize("hasRole(ADMIN)")
@RequiredArgsConstructor
public class AdminController {
	
	@NonNull
	private AdminStatService statServ;
	@NonNull
	private NotificationService notiServ;
	@NonNull
	private MsgService msgServ;
	
	@GetMapping("")
	public String showAdminInfo(Model model){
		model.addAttribute("postCnt", statServ.getPostCount('d'));
		model.addAttribute("userLeaveCnt", statServ.getUserLeaveCount('d'));
		model.addAttribute("userJoinCnt", statServ.getUserJoinCount('d'));
		return "";
	}
	
	@GetMapping("/noti")
	public String showNotification(Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			model.addAttribute("noti", notiServ.getNotifications(auth.getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@GetMapping("/msg")
	public String showMsg(Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			model.addAttribute("msg", msgServ.getMsgList(auth.getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	@GetMapping("/user")
	public String manageUser(){
		return "";
	}
	@GetMapping("/post")
	public String managePost(){
		return "";
	}
	@GetMapping("/reply")
	public String manageReply(){
		return "";
	}
	@GetMapping("/info")
	public String showAdminInfo(){
		return "";
	}
	
	
}
