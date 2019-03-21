package kr.co.ex.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.NotificationService;
import kr.co.ex.service.UserLikeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/daily")
public class BoardContoller {
	
	@NonNull private BoardService boardServ;
	@NonNull private UserLikeService likeServ;
	@NonNull private NotificationService notiServ;
	
	// 이미지 파일이 저장되는 루트 경로		
	@Resource
	String uploadPath;
	
	/*
	* @param cri 몇 번째 page인지, page마다 몇 개의 글을 보여주는지, 검색 타입, 검색 키워드
	*/
	
	// 검색결과 유지
	@GetMapping("")
	public String showPostList(@ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			PageMaker pageMaker = new PageMaker();
			
			int totalCount = 0;
			List<BoardVO> boardList = null;
			
			String keyword = cri.getKeyword();
			if(keyword == null){
				totalCount = boardServ.getTotalCnt(cri);
				boardList = boardServ.listCriteria(cri);
			}
			else{
				totalCount = boardServ.getSearchCnt(cri);
				boardList = boardServ.listSearch(cri);
			}
			
			pageMaker.setCri(cri);
			pageMaker.setTotalCount(totalCount);
			
			model.addAttribute("pagination", pageMaker);
			model.addAttribute("boardList", boardList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "board/daily.part";
	}
	
	@GetMapping("/{boardNo}/like")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> updatePostLike(@PathVariable int boardNo, @RequestParam int likeCnt){
		try {			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			boardServ.updateLike(boardNo, likeCnt, auth.getName());
			
		} catch (Exception e) {
			new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/{boardNo}/report")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> updateReportCnt(@PathVariable int boardNo, @RequestParam int diff){
		try{
			boardServ.updateReportCnt(boardNo, diff);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/new")
	public String regist(){
		return "board/new.part";
	}
	
	@PostMapping("/new")
	public String registerPost(BoardVO board, RedirectAttributes attrs){
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			if(auth != null) board.setWriter(auth.getName());
			
			boardServ.register(board);
			attrs.addFlashAttribute("msg", "success");
		
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/daily";
	}
	
	// 검색결과 유지
	@GetMapping("/{boardNo}")
	public String showEachPost(@PathVariable int boardNo, 
			@RequestParam(required=false) String from, @RequestParam(required=false) Integer nno,
			@ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			boardServ.updateViewCnt(boardNo);
			if(from != null && from.equals("noti")) notiServ.markReadFlagNotification(nno);
			
			model.addAttribute("isUserLiked", likeServ.isUserLiked(boardNo));
			model.addAttribute("replyCnt", boardServ.getReplyCnt(boardNo));
			
			BoardVO board =  boardServ.read(boardNo);
			model.addAttribute("board", board);
			model.addAttribute("specificTitle", board.getTitle());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "board/post.part";
	}
	
	@GetMapping(value="/{boardNo}/attach", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<AttachVO> addAttachToPost(@PathVariable int boardNo) throws Exception{
		List<AttachVO> attaches = boardServ.getAttach(boardNo);
		return attaches;
	}
	
	// 검색어 유지
	@GetMapping("/{boardNo}/mod")
	@PreAuthorize("isAuthenticated()")
	public String modifyPost(@PathVariable int boardNo, @ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			BoardVO board = boardServ.read(boardNo);
			model.addAttribute("board", board);
			model.addAttribute("specificTitle", board.getTitle());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "board/modify.part";
	}
	
	// 검색어 유지
	@PostMapping("/{boardNo}")
	@PreAuthorize("isAuthenticated()")
	public String modifyPost(BoardVO board, SearchCriteria cri, RedirectAttributes attrs){
		try {
			boardServ.modify(board);
			attrs.addFlashAttribute("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/daily"+cri.makeSearch();
	}
	/*
	@DeleteMapping("/{boardNo}")
	@PreAuthorize("principal.username == #name")
	public String deletePost(@PathVariable Integer boardNo, @RequestParam String username, SearchCriteria cri, RedirectAttributes attrs){
		try{
			this.deleteFile(boardServ.getAttach(boardNo));
			boardServ.remove(boardNo);
			attrs.addFlashAttribute("msg", "success");
		} catch(Exception e){
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/daily"+cri.makeSearch();
	}
	*/
	private void deleteFile(List<AttachVO> attaches){
		if(attaches == null || attaches.size() == 0) return;
		attaches.forEach(attach -> {
			log.info(attach.toString());
			try {
				Path file = Paths.get(uploadPath+attach.getUploadPath()+"\\"+attach.getUuid()+"_"+attach.getFileName());
				Files.deleteIfExists(file);
				if(Files.probeContentType(file).startsWith("image")){
					Path thumbnail = Paths.get(uploadPath+attach.getUploadPath()+"\\s_"+attach.getUuid()+"_"+attach.getFileName());
					Files.deleteIfExists(thumbnail);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
