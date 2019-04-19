package kr.co.ex.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kr.co.ex.domain.AuthVO;
import kr.co.ex.dto.LoginDto;
import kr.co.ex.service.LoginService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequiredArgsConstructor
public class LoginController {

	@NonNull LoginService serv;
	
	@GetMapping("/accessError")
	public void accessError(){
		
	}

	@GetMapping("/board/login")
	public String login(){
		return "login/login.part";	
	}	
	
	@GetMapping("/board/login/admin")
	public String adminLogin(){
		return "login/adminLogin.part";	
	}	

	@GetMapping("/board/signin")
	public String signin(){
		return "redirect:/board/oauth2/authorization/google";
	}
	
	@PostMapping("/board/signin")
	@PreAuthorize("isAnoymous()")
	public String signin(LoginDto user){	
		List<AuthVO> auth = new ArrayList<>();
		user.setAuths(auth);
		serv.signIn(user);
		return "redirect:/";
	}
	
	@GetMapping("/board/logout")
	@PreAuthorize("isAuthenticated()")
	public String logout(){
		return "login/logout.page";
	}
	
	/*
	@GetMapping("/board/oauth2/login")
	public String getGoogleCode(HttpServletRequest req){
		log.info(SecurityContextHolder.getContext().getAuthentication().getName());
		return "login/google";
	} 
	 
	@PostMapping("/login")
	public String login(LoginDto dto, HttpSession sess, Model model) throws Exception{
		UserVO user = serv.login(dto);
		if(user == null) return "/blank";
		else{
			model.addAttribute("userVO", user);
			int week = 60*60*24*7;
			// 1000�� ����
			Date sessionlimit = new Date(System.currentTimeMillis()+(1000*week));
			if(dto.isUseCookie()) serv.keepLogin(sess.getId(), sessionlimit, user.getUid());
			return "/blank";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest req, HttpServletResponse res, HttpSession sess) throws Exception{
		Object obj = sess.getAttribute("login");
		if(obj != null){
			UserVO vo = (UserVO) obj;
			sess.removeAttribute("login");
			sess.invalidate();
			Cookie loginCookie = WebUtils.getCookie(req, "loginCookie");
			if(loginCookie != null){
				loginCookie.setPath("/");
				loginCookie.setMaxAge(0);
				res.addCookie(loginCookie);
				serv.keepLogin(sess.getId(), new Date(), vo.getUid());
			}
		}
		return "redirect:/";
	}
	*/
}
