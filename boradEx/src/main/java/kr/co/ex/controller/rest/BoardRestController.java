package kr.co.ex.controller.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.service.BoardService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board/api")
public class BoardRestController {
	
	@NonNull private BoardService boardServ;
	@Resource String uploadPath;
	
	@GetMapping(value="", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<BoardVO>> getPosts(Criteria cri){
		List<BoardVO> posts = null;
		try {
			posts = boardServ.listByWriter(SecurityContextHolder.getContext().getAuthentication().getName(), cri);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}
	
	@PostMapping("/mod")
	public ResponseEntity<Void> modifyPost(@RequestParam int bno, @RequestParam String password, SearchCriteria cri){
		try{
			if(boardServ.matchPassword(bno, password)){
				return new ResponseEntity<>(HttpStatus.OK); 
			}
			else throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
		} catch(AccessDeniedException e){
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	
	}
	
	@PostMapping("/rem")
	public ResponseEntity<List<BoardVO>> deletePost(List<Integer> bnoList, SearchCriteria cri){
		List<BoardVO> list = null;
		try{
			for(int bno : bnoList){
				BoardVO vo = new BoardVO();
				vo.setBno(bno);
				boardServ.remove(vo);
				this.deleteFile(boardServ.getAttach(bno));
			}
			list = boardServ.listSearch(cri);
		} catch(AccessDeniedException e){
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@PostMapping("/rem")
	public ResponseEntity<Void> deletePost(BoardVO vo, SearchCriteria cri){
		try{
			boardServ.remove(vo);
			this.deleteFile(boardServ.getAttach(vo.getBno()));
		} catch(AccessDeniedException e){
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private void deleteFile(List<AttachVO> attaches){
		if(attaches == null || attaches.size() == 0) return;
		attaches.forEach(attach -> {
			log.info(attach.toString());
			try {
				Path file = Paths.get(uploadPath+attach.getUploadPath()+"\\"+attach.getUuid()+"_"+attach.getFileName());
				Files.deleteIfExists(file);
				if(attach.getFileType().startsWith("image")){
					Path thumbnail = Paths.get(uploadPath+attach.getUploadPath()+"\\s_"+attach.getUuid()+"_"+attach.getFileName());
					Files.deleteIfExists(thumbnail);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
