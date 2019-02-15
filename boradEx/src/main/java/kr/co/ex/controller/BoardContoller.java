package kr.co.ex.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import kr.co.ex.exception.BadLikeUpdateException;
import kr.co.ex.service.BoardService;
import kr.co.ex.service.UserLikeService;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/board")
@Log4j
public class BoardContoller {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardContoller.class);
	
	@Autowired
	private BoardService boardServ;
	
	@Autowired
	private UserLikeService likeServ;
	
	@Resource
	String uploadPath;
	
	@GetMapping("/list")
	// @ModelAttribute working process(my guess) 
	// 1. create object 
	// 2. calling setter 
	// 3. added to model
	public String list(@ModelAttribute("cri") SearchCriteria cri, Model model){
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
		return "/board/slist";
	}
	
	@GetMapping("/like")
	public ResponseEntity<Void> updateLike(@RequestParam int bno, @RequestParam int diff, @RequestParam String username){
		
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			
			if(!auth.getName().equals(username)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			
			boardServ.updateLike(bno, diff, username);
		} catch (Exception e) {
			new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/register")
	public String regist(){
		return "/board/register";
	}
	
	@PostMapping("/register")
	@PreAuthorize("permitAll()")
	public String regist(BoardVO board, RedirectAttributes attrs){
		try {
			logger.info(board.toString());
			boardServ.register(board);
			attrs.addFlashAttribute("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/list";
	}
	
	@GetMapping("/list/{bno}")
	public String readDetail(@PathVariable Integer bno, @ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			log.info("board controller "+bno);
			boardServ.updateViewCnt(bno);
			model.addAttribute("replyCnt", boardServ.getReplyCnt(bno));
			model.addAttribute(boardServ.read(bno));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/read";
	}
	
	@GetMapping(value="/getAttach/{bno}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<AttachVO> getAttach(@PathVariable Integer bno) throws Exception{
		List<AttachVO> attaches = boardServ.getAttach(bno);
		log.info(attaches);
		return attaches;
	}
	
	@GetMapping("/modify")
	@PreAuthorize("principal.username == #name")
	public String read(@RequestParam Integer bno, @RequestParam String name, @ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			model.addAttribute(boardServ.read(bno));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/modify";
	}
	
	@PostMapping("/modify")
	@PreAuthorize("principal.username == #vo.writer")
	public String modify(@RequestParam Integer bno, BoardVO vo, SearchCriteria cri, RedirectAttributes attrs){
		try {
			log.info(vo.toString());
			boardServ.modify(vo);
			attrs.addFlashAttribute("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/list"+cri.makeSearch();
	}
	
	@PostMapping("/delete")
	@PreAuthorize("principal.username == #name")
	public String delete(@RequestParam Integer bno, @RequestParam String name, SearchCriteria cri, RedirectAttributes attrs){
		try{
			deleteFile(boardServ.getAttach(bno));
			boardServ.remove(bno);
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
