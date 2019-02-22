package kr.co.ex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.ex.domain.MsgVO;
import kr.co.ex.service.MsgService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequiredArgsConstructor
public class MsgController {

	@NonNull
	private MsgService msgServ;
	
	@PostMapping("/board/msg")
	public ResponseEntity<Void> registerMsg(MsgVO vo){
		try {
			msgServ.registerMsg(vo);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PatchMapping("/board/msg/{msgNo}")
	public ResponseEntity<Void> setDeleteFlag(@RequestParam String type, @PathVariable int msgNo){
		try{
			if(type.equals("receiver")) msgServ.setReceiverDeleteFlag(msgNo);
			else if(type.equals("sender")) msgServ.setSenderDeleteFlag(msgNo);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
