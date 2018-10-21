package kr.co.ex.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.WebUtils;

import kr.co.ex.domain.UserVO;
import kr.co.ex.dto.LoginDto;
import kr.co.ex.service.LoginService;

@Controller
public class LoginController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	LoginService serv;
	
	@GetMapping("/login")
	public void login(){
	}
	
	@PostMapping("/loginPost")
	public String login(LoginDto dto, HttpSession sess, Model model) throws Exception{
		UserVO user = serv.login(dto);
		if(user == null) return "/blank";
		else{
			model.addAttribute("userVO", user);
			int week = 60*60*24*7;
			// 1000¿œ ±‚¡ÿ
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
}
