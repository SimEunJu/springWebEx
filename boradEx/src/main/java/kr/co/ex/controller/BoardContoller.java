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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.UserLikeService;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/board/daily")
@Log4j
public class BoardContoller {
	
	@Setter
	private BoardService boardServ;
	
	@Setter
	private UserLikeService likeServ;
	
	// 이미지 파일이 저장되는 루트 경로		
	@Resource
	String uploadPath;
	
	// @param cri 몇 번째 page인지, page마다 몇 개의 글을 보여주는지, 검색 타입, 검색 키워드
	@GetMapping("/")
	public String showPostList(@ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			PageMaker pageMaker = new PageMaker();
			
			int totalCount = 0;
			List<BoardVO> boardList = null;
			
			String keyword = cri.getKeyword();
			if(keyword == null || keyword.trim().length() == 0){
				totalCount = boardServ.getTotalCount();
				boardList = boardServ.listSearch(cri);
				pageMaker.setCri(cri);
			}
			else{
				totalCount = boardServ.getSearchCount(cri);
				boardList = boardServ.listSearch(cri);
				pageMaker.setCri(cri);
			}
			
			pageMaker.setTotalCount(totalCount);
			model.addAttribute("pageMaker", pageMaker);
			model.addAttribute("boardList", boardList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/daily";
	}
	
	@GetMapping("/{boardNo}/like")
	@PreAuthorize("principal.username == #username")
	public ResponseEntity<Void> updatePostLike(@PathVariable Integer boardNo, @RequestParam Integer likeCnt, @RequestParam String username){
		try {			
			boardServ.updateLike(boardNo, likeCnt, username);
		} catch (Exception e) {
			log.info(e);
			new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/new")
	public String regist(){
		return "/board/new";
	}
	
	@PostMapping("/new")
	public String registerPost(BoardVO board, RedirectAttributes attrs){
		try {
			log.info(board.toString());
			boardServ.register(board);
			attrs.addFlashAttribute("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/daily";
	}
	
	@GetMapping("/{boardNo}")
	public String showEachPost(@PathVariable Integer boardNo, @ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			boardServ.updateViewCnt(boardNo);
			model.addAttribute("replyCnt", boardServ.getReplyCnt(boardNo));
			model.addAttribute(boardServ.read(boardNo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/read";
	}
	
	@GetMapping(value="/{boardNo}/attach", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<AttachVO> addAttachToPost(@PathVariable Integer boardNo) throws Exception{
		List<AttachVO> attaches = boardServ.getAttach(boardNo);
		log.info(attaches);
		return attaches;
	}
	
	@GetMapping("/")
	@PreAuthorize("principal.username == #name")
	public String modifyPost(@RequestParam Integer bno, @RequestParam String name, @ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			model.addAttribute(boardServ.read(bno));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/modify";
	}
	
	@PutMapping("/{boardNo}")
	@PreAuthorize("principal.username == #vo.writer")
	public String modify(@PathVariable Integer bno, BoardVO board, SearchCriteria cri, RedirectAttributes attrs){
		try {
			log.info(board.toString());
			boardServ.modify(board);
			attrs.addFlashAttribute("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/list"+cri.makeSearch();
	}
	
	@DeleteMapping("/{boardNo}")
	@PreAuthorize("principal.username == #name")
	public String delete(@PathVariable Integer boardNo, @RequestParam String username, SearchCriteria cri, RedirectAttributes attrs){
		try{
			this.deleteFile(boardServ.getAttach(boardNo));
			boardServ.remove(boardNo);
			attrs.addFlashAttribute("msg", "success");
		} catch(Exception e){
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/list"+cri.makeSearch();
	}
	
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
