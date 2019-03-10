package kr.co.ex.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.ex.domain.Criteria;
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
	
	@PostMapping(value = "/board/user/msg")
	public ResponseEntity<Map<String,String>> registerMsg(@RequestBody Map<String, Object> param) {
		
		List<String> receivers = (List<String>) param.get("receivers");
	
		MsgVO vo = new MsgVO();
		vo.setContent((String) param.get("content"));
		vo.setTitle((String) param.get("title"));
		vo.setSender(SecurityContextHolder.getContext().getAuthentication().getName());

		try {
			msgServ.registerMsgList(receivers, vo);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(Collections.singletonMap("result", "success"), HttpStatus.OK);
	}
	
	@PutMapping("/board/user/msg/{msgNo}")
	public ResponseEntity<Void> setDeleteFlagOnMsg(@RequestParam String type, @PathVariable int msgNo){
		try{
			if(type.equals("receiver")) msgServ.setReceiverDeleteFlag(new ArrayList<>(msgNo));
			else if(type.equals("sender")) msgServ.setSenderDeleteFlag(new ArrayList<>(msgNo));
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(value="/board/user/msg/{msgNo}", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	public ResponseEntity<MsgVO> getMsg(@PathVariable int msgNo){
		try {
			return new ResponseEntity<>(msgServ.getMsg(msgNo), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value = "/board/user/msg/del", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<MsgVO>> deleteMsges(@RequestBody ArrayList<Integer> msgNo){
		try {
			log.info(msgNo);
			msgServ.setReceiverDeleteFlag(msgNo);
			String curUser = SecurityContextHolder.getContext().getAuthentication().getName();
			log.info(curUser);
			List<MsgVO> msgList = msgServ.getMsgList(curUser, new Criteria());
			log.info(msgList);
			return new ResponseEntity<>(msgList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
