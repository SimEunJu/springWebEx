package kr.co.ex.controller.rest.board;

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
import org.springframework.web.bind.annotation.RestController;

import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.service.BoardService;
import kr.co.ex.util.file.DeleteFileUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardRestController {
	
	@NonNull private BoardService boardServ;
	@Resource private String uploadPath;
	
	@GetMapping(value="", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<BoardVO>> getPostListByWriter(SearchCriteria cri){
		List<BoardVO> posts = null;
		try {
			posts = boardServ.listByWriter(SecurityContextHolder.getContext().getAuthentication().getName(), cri);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}
	
	private String getDeleteReason(String reason){
		switch (reason) {
		case "report":
			return "신고 횟수의 누적으로 삭제 처리 되었습니다.";
		case "inappropriate":
			return "부적절한 내용을 담고 있기 때문에 삭제 처리 되었습니다.";
		default:
			return null;
		}
	}
	
	@PostMapping(value="/admin/rem", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<BoardVO>> deletePostByAdmin(@RequestBody Map<String, Object> param, SearchCriteria cri){
		List<BoardVO> list = null;
		try{
			List<String> bnoList = (List<String>) param.get("bnoList");
			String reason = (String) param.get("reason");
			
			for(String boardNo : bnoList){
				int bno = Integer.parseInt(boardNo);
				BoardVO vo = new BoardVO();
				vo.setBno(bno);
				vo.setContent(getDeleteReason(reason));
				boardServ.remove(vo);
				DeleteFileUtils.deleteFiles(boardServ.getAttach(bno));
			}
			list = boardServ.listSearch(cri);
			
		} catch(AccessDeniedException e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			
		} catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	
}
