package kr.co.ex.controller.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	@Resource private String uploadPath;
	
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
	
	private String getReason(String val){
		switch (val) {
		case "report":
			return "신고 횟수의 누적으로 삭제 처리 되었습니다.";
		case "inappropriate":
			return "부적절한 내용을 담고 있기 때문에 삭제 처리 되었습니다.";
		default:
			return null;
		}
	}
	
	@PostMapping("/admin/rem")
	@PreAuthorize("hasRole('ADMIN'")
	public ResponseEntity<List<BoardVO>> deletePost(@RequestBody Map<String, Object> param, SearchCriteria cri){
		List<BoardVO> list = null;
		try{
			List<String> bnoList = (List<String>) param.get("bnoList");
			String reason = (String) param.get("reason");
			for(String boardNo : bnoList){
				int bno = Integer.parseInt(boardNo);
				BoardVO vo = new BoardVO();
				vo.setBno(bno);
				vo.setContent(getReason(reason));
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
