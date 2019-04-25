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
		// �α��ε� ȸ���� ���
		List<GrantedAuthority> auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
			.stream().filter(a -> "ROLE_USER".equals(a.getAuthority())).collect(Collectors.toList());
		if(auth.size() != 0) return true;
		
		// �͸� ȸ���� ���
		// �͸� ȸ�� ��й�ȣ ������ �� �� ���
		HttpSession sess = request.getSession();
		Boolean isAuthenticatedAnony = (Boolean) sess.getAttribute("isAuthenticatedAnony");
		
		if(isAuthenticatedAnony == null){
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}
		
		// ���� ������ �͸� ȸ���� ���
		if(isAuthenticatedAnony == false){
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}
		
		// ���� ������ �͸� ȸ���� ���
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
