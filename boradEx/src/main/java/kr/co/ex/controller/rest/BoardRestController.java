package kr.co.ex.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.service.BoardService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController
@RequiredArgsConstructor
@Log4j
@RequestMapping("/board/api")
public class BoardRestController {
	
	@NonNull
	private BoardService boardServ;
	
	@GetMapping(value="", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<BoardVO>> getPosts(Criteria cri){
		List<BoardVO> posts = null;
		try {
			posts = boardServ.listBoardByWriter(SecurityContextHolder.getContext().getAuthentication().getName(), cri);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}
	
}
