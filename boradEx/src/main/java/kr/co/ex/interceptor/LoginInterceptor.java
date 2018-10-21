package kr.co.ex.interceptor;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	private static final String LOGIN = "login";
	private static final int WEEK = 60*60*24*7;
	
	@Override
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
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		HttpSession sess = request.getSession();
		Object user = sess.getAttribute(LOGIN);
		if(user != null){
			sess.removeAttribute(LOGIN);
		}
		return true;
	}

}
