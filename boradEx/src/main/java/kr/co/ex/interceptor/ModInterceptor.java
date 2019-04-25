package kr.co.ex.interceptor;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.log4j.Log4j;

@Log4j
public class ModInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		// 로그인된 회원인 경우
		List<GrantedAuthority> auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
			.stream().filter(a -> "ROLE_USER".equals(a.getAuthority())).collect(Collectors.toList());
		if(auth.size() != 0) return true;
		
		// 익명 회원일 경우
		// 익명 회원 비밀번호 인증이 안 된 경우
		HttpSession sess = request.getSession();
		Boolean isAuthenticatedAnony = (Boolean) sess.getAttribute("isAuthenticatedAnony");
		
		if(isAuthenticatedAnony == null){
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}
		
		// 인증 실패인 익명 회원의 경우
		if(isAuthenticatedAnony == false){
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}
		
		// 인증 성공인 익명 회원의 경우
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(!"GET".equals(request.getMethod())){
			HttpSession sess = request.getSession();
			Boolean isAuthenticatedAnony = (Boolean) sess.getAttribute("isAuthenticatedAnony");
			if(isAuthenticatedAnony != null) sess.removeAttribute("isAuthenticatedAnony");
		}
	}
	
	
}
