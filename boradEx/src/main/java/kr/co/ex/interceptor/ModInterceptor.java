package kr.co.ex.interceptor;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.log4j.Log4j;

@Log4j
public class ModInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		List<GrantedAuthority> auth = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
			.stream().filter(a -> "ROLE_ADMIN".equals(a.getAuthority())).collect(Collectors.toList());
		if(auth.size() != 0) return true;
		HttpSession sess = request.getSession();
		Boolean isAnonyMod = (Boolean) sess.getAttribute("anonyMod");
		if(isAnonyMod == null) return true;
		
		if(isAnonyMod == false) return false;
		sess.removeAttribute("anonyMod");		
		return true;
	}

/*	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler, ModelAndView modelAndView) throws Exception {
		HttpSession sess = request.getSession();
		Map<String, Object> model = modelAndView.getModel();
		Object user = model.get("userVO");
		if(user !=  null){
			sess.setAttribute(LOGIN, user);
			
			if(request.getParameter("useCookie") != null){
				Cookie loginCookie = new Cookie("loginCookie", sess.getId());
				loginCookie.setPath("/");
				loginCookie.setMaxAge(WEEK);
				response.addCookie(loginCookie);
			}
			
			Object dest = sess.getAttribute("dset");
			response.sendRedirect(dest==null? "/" : (String)dest);
		}else{
			response.sendRedirect("/login");
		}
	}*/	
}
