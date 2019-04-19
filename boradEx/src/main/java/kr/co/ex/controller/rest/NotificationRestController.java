package kr.co.ex.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.MsgVO;
import kr.co.ex.domain.NotificationVO;
import kr.co.ex.dto.NotificationDto;
import kr.co.ex.service.NotificationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequestMapping("/board/user/noti")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class NotificationRestController {
	
	@NonNull private NotificationService notiServ;
	
	@PostMapping(value = "/del", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<NotificationDto>> deleteNoties(@RequestBody ArrayList<Integer> notiNo){
		try {
			notiServ.deleteNotifications(notiNo);
			
			String curUser = SecurityContextHolder.getContext().getAuthentication().getName();
			List<NotificationDto> notiList = notiServ.getNotifications(curUser, new Criteria());
			return new ResponseEntity<>(notiList, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
