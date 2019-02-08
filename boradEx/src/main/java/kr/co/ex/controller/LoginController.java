package kr.co.ex.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kr.co.ex.domain.AuthVO;
import kr.co.ex.dto.LoginDto;
import kr.co.ex.service.LoginService;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class LoginController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	LoginService serv;
	
	@GetMapping("/accessError")
	public void accessError(){
		
	}

	@GetMapping("/board/login")
	public String login(HttpServletRequest req){
		return "login/login";	
	}
	
	@GetMapping("/board/login/google")
	public String loginGoogle(HttpServletRequest req){
		Enumeration<String>	e = req.getAttributeNames();
		log.info("............................................");
		if(req.getParameter("code") != null){
			return "redirect:/board/list";
		}
		return "redirect:https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=https://localhost:8443/board/login/google&client_id=576530370417-8pa99jec06r4o6m4noonp3bui7t9rjpn.apps.googleusercontent.com&scope=email&response_type=code";	
	}
	
	@PostMapping("/board/signin")
	public String signin(LoginDto user){
		List<AuthVO> auth = new ArrayList<>();
		user.setAuths(auth);
		log.info(user);
		serv.signIn(user);
		return "redirect:/";
	}
	
	/*
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
