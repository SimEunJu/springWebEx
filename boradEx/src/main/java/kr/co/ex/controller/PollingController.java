package kr.co.ex.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.dto.PollingMsgDto;
import kr.co.ex.dto.PollingNotiDto;
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.PollingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/board/polling")
@PreAuthorize("isAuthenticated()")
public class PollingController {
	
	// 로직을 함수로 뽑아낼 수 없을까?
	
	@NonNull private PollingService pollServ;
	@NonNull private NotificationService notiServ;
	
	private final int LIMIT = 999;
	private final int DAY = 60*60*24;
	// 기본 polling 간격 30초
	private final int DEAFULT_TERM_SEC = 30;
	// 최대 polling 간격 10분
	private final int MAX_TERM_SEC = 20*60;
	// 최소 polling 간격 10초
	private final int MIN_TERM_SEC = 10;
	
	@GetMapping("/msg")
	public ResponseEntity<Void> pollingMsgCnt(@CookieValue(value="msgPoll", required=false) Cookie msgCk, 
			HttpServletResponse res) throws Exception{
		
		PollingMsgDto msg = null;
		int curCnt = 0;
		int msgNo = 0;
		int term = DEAFULT_TERM_SEC;
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(msgCk != null && !msgCk.getValue().isEmpty()){
			String ckVals[] = msgCk.getValue().split("z");
			msgNo = Integer.parseInt(ckVals[0]);
			curCnt = Integer.parseInt(ckVals[1]);
			term = Integer.parseInt(ckVals[2]);
			if(curCnt < LIMIT) msg = pollServ.getMsgCnt(new PollingMsgDto(msgNo, username, LIMIT-curCnt));
		}
		else{
			msg = pollServ.getMsgCnt(new PollingMsgDto(0, username, LIMIT));
		}
		
		int newTerm = calcTerm(msg.getCnt(), term);
		int newCnt = curCnt+msg.getCnt();
		int	newMsgNo = msg.getMsgNo() == 0 ? msgNo : msg.getMsgNo();
		
		String ckVal = newMsgNo+"z"+newCnt+"z"+newTerm;
		
		Cookie msgCookie = new Cookie("msgPoll", ckVal);
		msgCookie.setPath("/");
		msgCookie.setMaxAge(DAY);
		res.addCookie(msgCookie);
		
		log.info(msg.toString());
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/noti")
	public ResponseEntity<Integer> pollingNotiCnt(@CookieValue(value="notiPoll", required=false) Cookie notiCk,
			@RequestParam(required=false) Integer nno, HttpServletResponse res){

		PollingNotiDto noti = null;
		int curCnt = 0;
		int notiNo = 0;
		int term = DEAFULT_TERM_SEC;
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if(nno != null){
			try{
				if(nno != null) notiServ.markReadFlagNotification(nno);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(notiCk != null && !notiCk.getValue().isEmpty()){
			String ckVals[] = notiCk.getValue().split("z");
			notiNo = Integer.parseInt(ckVals[0]);
			curCnt = Integer.parseInt(ckVals[1]);
			term = Integer.parseInt(ckVals[2]);
			if(curCnt < LIMIT) noti = pollServ.getNotiCnt(new PollingNotiDto(notiNo, username, LIMIT-curCnt));
		}
		else{
			noti = pollServ.getNotiCnt(new PollingNotiDto(0, username, LIMIT));
		}
		
		int newTerm = calcTerm(noti.getCnt(), term);
		int newCnt = curCnt+noti.getCnt();
		int	newNNo = noti.getNno() == 0 ? notiNo : noti.getNno();
	
		String ckVal = newNNo+"z"+newCnt+"z"+newTerm;
		Cookie notiCookie = new Cookie("notiPoll", ckVal);
		notiCookie.setPath("/");
		notiCookie.setMaxAge(DAY);
		res.addCookie(notiCookie);
		
		log.info(noti.toString());
		return ResponseEntity.ok(null);
	}
	// 1. 일정 갯수 
	
	private int calcTerm(int cur, int term){
		// 0 <= diff <= 999
		int newTerm = (int)((1 - (cur/1000 - 0.5)) * term);

		if(newTerm > MAX_TERM_SEC) return MAX_TERM_SEC;
		else if(newTerm < MIN_TERM_SEC) return MIN_TERM_SEC;
		
		return newTerm;
	}
}
