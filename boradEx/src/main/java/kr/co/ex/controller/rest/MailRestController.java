package kr.co.ex.controller.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.mail.MailDto;
import kr.co.ex.mail.MailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController
@Log4j
@RequiredArgsConstructor
@RequestMapping("/board/api/admin/mail")
public class MailRestController {
	
	@NonNull
	private MailService mailServ;
	
	@Value("${mail.google.email}")
	private final String GOOGLE_EMAIL_SENDER;
	
	@PostMapping("")
	public ResponseEntity<String> sendGoogleMail(@RequestBody Map<String, Object> param){
		List<String> receivers = (List<String>) param.get("receivers");
		
		MailDto dto = new MailDto();
			
		dto.setTitle((String) param.get("title"));
		dto.setContent((String) param.get("content"));
		dto.setFrom(GOOGLE_EMAIL_SENDER);
		dto.setToList(receivers);
				
		boolean isMailSend = mailServ.send(dto);
		
		if(isMailSend == false) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
}
