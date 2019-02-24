package kr.co.ex.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.ex.domain.Criteria;
import kr.co.ex.service.AdminStatService;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.MsgService;
import kr.co.ex.service.NotificationService;
import kr.co.ex.util.DateUtils;
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
	@NonNull
	private BoardService boardServ;
	
	@GetMapping("")
	public String showAdminInfo(@RequestParam(required=false, defaultValue="d") char type, Model model){
		try{
			model.addAttribute("postCnt", statServ.getPostCount(type));
			model.addAttribute("userLeaveCnt", statServ.getUserLeaveCount(type));
			model.addAttribute("userJoinCnt", statServ.getUserJoinCount(type));
			LocalDateTime defaultDate = DateUtils.getAFewWeeksAgo(1);
			model.addAttribute("hotPost", boardServ.listRegdate(defaultDate));
		}catch(Exception e){
			e.printStackTrace();
		}
		return "dashBoard/admin/adminMain";
	}
	
	@GetMapping("/noti")
	public String showNotification(Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			model.addAttribute("noti", notiServ.getNotifications(auth.getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dashBoard/noti";
	}
	
	@GetMapping("/msg")
	public String showMsg(Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			model.addAttribute("msg", msgServ.getMsgList(auth.getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dashBoard/msg";
	}
	@GetMapping("/user")
	public String manageUser(Criteria cri, Model model){
		
		return "dashBoard/admin/userManage";
	}
	@GetMapping("/post")
	public String managePost(Criteria cri, Model model){
		return "dashBoard/post";
	}
	@GetMapping("/reply")
	public String manageReply(Criteria cri, Model model){
		return "dashBoard/reply";
	}
	
	@GetMapping("/info")
	public String showAdminInfo(Model model){
		return "dashBoard/admin/adminInfo";
	}
	
	
}
