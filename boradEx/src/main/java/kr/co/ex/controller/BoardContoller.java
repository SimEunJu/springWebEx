package kr.co.ex.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.PageMaker;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardContoller {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardContoller.class);
	@Autowired
	private BoardService serv;
	
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
				totalCount = serv.getTotalCount();
				boardList = serv.listCriteria(cri);
				pageMaker.setCri((Criteria)cri);
			}
			else{
				totalCount = serv.getSearchCount(cri);
				boardList = serv.listSearch(cri);
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
	
	@GetMapping("/register")
	public String regist(){
		return "/board/register";
	}
	
	@PostMapping("/register")
	public String regist(BoardVO board, RedirectAttributes attrs){
		try {
			logger.info(board.toString());
			serv.register(board);
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
			serv.updateViewCnt(bno);
			model.addAttribute("replyCnt", serv.getReplyCnt(bno));
			model.addAttribute(serv.read(bno));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/read";
	}
	
	@GetMapping("/getAttach/{bno}")
	@ResponseBody
	public List<String> getAttach(@PathVariable Integer bno) throws Exception{
		return serv.getAttach(bno);
	}
	
	@GetMapping("/modify")
	public String read(@RequestParam Integer bno, @ModelAttribute("cri") SearchCriteria cri, Model model){
		try {
			model.addAttribute(serv.read(bno));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/board/modify";
	}
	
	@PostMapping("/modify")
	public String modify(@RequestParam Integer bno, BoardVO vo, SearchCriteria cri, RedirectAttributes attrs){
		try {
			serv.modify(vo);
			attrs.addFlashAttribute("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/list"+cri.makeSearch();
	}
	
	@PostMapping("/delete")
	public String delete(@RequestParam Integer bno, SearchCriteria cri, RedirectAttributes attrs){
		try{
			serv.remove(bno);
			attrs.addFlashAttribute("msg", "success");
		} catch(Exception e){
			e.printStackTrace();
			attrs.addFlashAttribute("msg", "fail");
		}
		return "redirect:/board/list"+cri.makeSearch();
	}
}
