package kr.co.ex.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.dto.PollingMsgDto;
import kr.co.ex.service.MsgService;
import kr.co.ex.service.PollingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController
@Log4j
@RequiredArgsConstructor
@RequestMapping("/board/polling")
@PreAuthorize("isAuthenticated()")
public class PollingController {
	
	@NonNull
	private PollingService pollServ;
	@NonNull
	private MsgService msgServ;
	
	private final int LIMIT = 999;
	private final int DAY = 60*60*24;
	// 기본 polling 간격 10분
	private final int DEAFULT_TERM = 10;
	// 최대 polling 간격 2시간
	private final int MAX_TERM = 120;
	// 최소 polling 간격 2분
	private final int MIN_TERM = 2;
	
	@GetMapping("/msg")
	public ResponseEntity<Void> pollingMsgCnt(@CookieValue(value="msgPoll", required=false) Cookie msgCk, 
			HttpServletResponse res) throws Exception{
		
		PollingMsgDto msg = null;
		int curCnt = 0;
		int msgNo = 0;
		int term = DEAFULT_TERM;
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(msgCk != null){
			String ckVals[] = msgCk.getValue().split("z");
			msgNo = Integer.parseInt(ckVals[0]);
			curCnt = Integer.parseInt(ckVals[1]);
			term = Integer.parseInt(ckVals[2]);
			if(curCnt < LIMIT) msg = pollServ.getMsgCnt(new PollingMsgDto(msgNo, username, LIMIT-curCnt));
		}
		else{
			msg = pollServ.getMsgCnt(new PollingMsgDto(0, username, LIMIT));
		}
		
		int newTerm = calcTerm(curCnt, msg.getCnt(), term);
		int newCnt = curCnt+msg.getCnt();
		int	newMsgNo = msg.getMsgNo() == 0 ? msgNo : msg.getMsgNo();
		
		String ckVal = newMsgNo+"z"+newCnt+"z"+newTerm;
		
		Cookie msgCookie = new Cookie("msgPoll", ckVal);
		msgCookie.setPath("/board");
		msgCookie.setMaxAge(DAY);
		res.addCookie(msgCookie);
		
		log.info(msg.toString());
		return ResponseEntity.ok(null);
	}
	@GetMapping("/noti")
	public ResponseEntity<Integer> pollingNotiCnt(HttpServletRequest req){
		
		return new ResponseEntity<>(0, HttpStatus.OK);
	}
	
	private int calcTerm(int prev, int cur, int term){
		// 0 <= diff <= 999
		int diff = cur - prev;
		int newTerm = (int)(1 - (diff/1000 - 0.5)) * term;
		if(newTerm > MAX_TERM) return MAX_TERM;
		else if(newTerm < MIN_TERM) return MIN_TERM;
		return newTerm;
	}
}
