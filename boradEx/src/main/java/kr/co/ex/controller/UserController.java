package kr.co.ex.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.dto.NotificationDto;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.MemberService;
import kr.co.ex.service.MsgService;
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.ReplyService;
import kr.co.ex.util.PaginationUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

	@NonNull private MemberService memServ;
	@NonNull private NotificationService notiServ;
	@NonNull private MsgService msgServ;
	@NonNull private BoardService boardServ;
	@NonNull private ReplyService replyServ;
	
	@GetMapping("")
	public String showUserMain(@RequestParam(required=false) String move, Criteria cri, Model model){
		return "dashBoard/user/userMain.page";
	}
	
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
	
	@GetMapping("/noti")
	public String showNotification(@RequestParam(required=false) String move, Criteria cri, Model model){
		try {	
			// 서버에서 사용자 이름을 가져온다
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			PageMaker pm = null;
			if(move != null) pm = PaginationUtils.pagination(move, cri, notiServ.getNotiCntByUsername(auth.getName()));
	
			List<NotificationDto> noti = notiServ.getNotifications(auth.getName(), cri);
			
			model.addAttribute("noties", noti);
			model.addAttribute("pagination", pm);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "dashBoard/noti.page";
	}

	@GetMapping("/post")
	public String showBoard(@RequestParam(required=false) String move, Criteria cri, Model model){
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			PageMaker pm = PaginationUtils.pagination(move, cri, boardServ.getTotalCntByWriter(auth.getName()));
			
			model.addAttribute("pagination", pm);
			model.addAttribute("posts", boardServ.listByWriter(auth.getName(), cri));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dashBoard/post.page";
	}
	
	@GetMapping("/reply")
	public String showReply(@RequestParam(required=false) String move, Criteria cri, Model model){
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			PageMaker pm = PaginationUtils.pagination(move, cri, replyServ.getTotalCntByReplyer(auth.getName()));
			
			model.addAttribute("pagination", pm);
			model.addAttribute("replies", replyServ.listReplyByWriter(auth.getName(), cri));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dashBoard/reply.page";
	}
	
	@GetMapping("/msg")
	public String showMsg(@RequestParam(required=false) String move, Criteria cri, Model model){
			try {
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			PageMaker pm = PaginationUtils.pagination(move, cri, msgServ.getReceiverTotalCnt(auth.getName()));
			
			model.addAttribute("msges", msgServ.getMsgList(auth.getName(), cri));
			model.addAttribute("pagination", pm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "dashBoard.msg.page";
	}
	
	@GetMapping("/info")
	public String showUserInfo(@RequestParam(required=false) String move, Criteria cri, Model model){
		return "dashBoard/user/userInfo.page";
	}
}
