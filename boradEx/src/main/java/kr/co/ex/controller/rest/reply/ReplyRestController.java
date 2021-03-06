package kr.co.ex.controller.rest.reply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.ReplyService;
import kr.co.ex.util.PaginationUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
public class ReplyRestController {

	@NonNull private BoardService boardServ;
	@NonNull private ReplyService replyServ;
	@NonNull private NotificationService notiServ;
	
	// rno ���� �� bad request
	@GetMapping(value="/{boardNo}", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<Map<String, Object>> getReplyList(@PathVariable int boardNo, Criteria cri){
		try {
			log.info(cri.toString());
			boolean notincludeAddedReplyToCnt = true;
			PageMaker pm = PaginationUtils.pagination(cri, replyServ.getTotalCount(boardNo, notincludeAddedReplyToCnt));
			
			Map<String, Object> map = new HashMap<>();
			map.put("pagination", pm);
			map.put("replies", replyServ.listCriteriaReply(boardNo, cri));
		
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/{boardNo}")
	public ResponseEntity<Void> registerReply(@PathVariable int boardNo, @RequestBody ReplyVO vo){
		try{
			log.info(vo.toString());
			replyServ.addReply(vo);
			notiServ.registerNotification(vo, boardNo);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value="/user", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<ReplyVO>> getReplyListByWriter(Criteria cri){
		List<ReplyVO> replies = null;
		try {
			replies = replyServ.listReplyByWriter(cri);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(replies, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/del", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<ReplyVO>> deleteReplyList(@RequestBody List<Integer> rno,
			@RequestParam(defaultValue="self") String type){
		List<ReplyVO> replies = null;
		try {
			Criteria cri = new Criteria();
			switch(type){
			case "self":
				replyServ.removeReplies(rno);
				replies = replyServ.listReplyByWriter(cri);
				break;
			case "report":
				replyServ.removeReplies(rno);
				replies = replyServ.listReplyByReportCnt(cri);
				break;
			default:
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(replies,HttpStatus.OK);
	}

}
