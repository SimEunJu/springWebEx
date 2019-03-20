package kr.co.ex.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.dto.NotificationDto;
import kr.co.ex.service.AdminStatService;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.MemberService;
import kr.co.ex.service.MsgService;
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.ReplyService;
import kr.co.ex.util.DateUtils;
import kr.co.ex.util.PaginationUtils;
import kr.co.ex.util.UserType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/board/admin")
@Controller
//@PreAuthorize("hasRole(ADMIN)")
@RequiredArgsConstructor
public class AdminController {
	
	@NonNull
	private AdminStatService statServ;
	@NonNull
	private NotificationService notiServ;
	@NonNull
	private MemberService memServ;
	
	// °øÅë
	@NonNull
	private MsgService msgServ;
	@NonNull
	private BoardService boardServ;
	@NonNull
	private ReplyService replyServ;
	
	@GetMapping("")
	public String showAdminInfo(@RequestParam(required=false, defaultValue="d") String type, Model model){
		try{
			model.addAttribute("postCnt", statServ.getPostCount(type));
			model.addAttribute("userLeaveCnt", statServ.getUserLeaveCount(type));
			model.addAttribute("userJoinCnt", statServ.getUserJoinCount(type));
			model.addAttribute("visitCnt", statServ.getVisitCount(type));
			LocalDateTime defaultDate = DateUtils.getAFewWeeksAgo(1);
			model.addAttribute("hotPost", boardServ.listRegdate(defaultDate));
		}catch(Exception e){
			e.printStackTrace();
		}
		return "dashBoard/admin/adminMain.page";
	}
	
	@GetMapping("/noti")
	public String showNotification(@RequestParam(required=false) String move, Criteria cri, Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		try {	
			List<NotificationDto> noti = notiServ.getNotifications(auth.getName(), cri);
			
			PageMaker pm = null;
			if(move != null) pm = PaginationUtils.pagination(move, cri, notiServ.getNotiCntByUsername(auth.getName()));
	
			model.addAttribute("noties", noti);
			model.addAttribute("pagination", pm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dashBoard/noti.page";
	}
	
	@GetMapping("/user")
	public String manageUser(@RequestParam(required=false) String move,Model model){
		Criteria cri = new Criteria();
		PageMaker pm = PaginationUtils.pagination(move, cri, memServ.getMemberCnt());
		
		model.addAttribute("pagination", pm);
		model.addAttribute("users", memServ.ListCategorizedMember(UserType.ALL, cri));
		return "dashBoard/admin/userManage.page";
	}
	
	@GetMapping("/info")
	public String showAdminInfo(Model model){
		return "dashBoard/admin/adminInfo.page";
	}

	@GetMapping("/msg")
	public String showMsg(@RequestParam(required=false) String move, Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Criteria cri = new Criteria();
			PageMaker pm = PaginationUtils.pagination(move, cri, msgServ.getReceiverTotalCnt(auth.getName()));
			model.addAttribute("msges", msgServ.getMsgList(auth.getName(), cri));
			model.addAttribute("pagination", pm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dashBoard/msg.page";
	}
	
	@GetMapping("/post")
	public String managePost(@RequestParam(required=false) String move, Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			PageMaker pm = PaginationUtils.pagination(move, new Criteria(), boardServ.getTotalCntByWriter(auth.getName()));
			
			model.addAttribute("pagination", pm);
			model.addAttribute("posts", boardServ.listBoardByWriter(auth.getName(), pm.getCri()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dashBoard/post.page";
	}

	@GetMapping("/reply")
	public String manageReply(@RequestParam(required=false) String move, Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			PageMaker pm = PaginationUtils.pagination(move, new Criteria(), replyServ.getTotalCntByReplyer(auth.getName()));
			
			model.addAttribute("pagination", pm);
			model.addAttribute("replies", replyServ.listReplyByWriter(auth.getName(), pm.getCri()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dashBoard/reply.page";
	}
}
