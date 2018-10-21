package kr.co.ex.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import kr.co.ex.domain.UserVO;
import kr.co.ex.service.LoginService;

public class AuthInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
	private static final String LOGIN = "login";

	@Autowired
	LoginService serv;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession sess = request.getSession();
		if(sess.getAttribute(LOGIN) == null){
			saveDest(request);
			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");
			if(loginCookie != null){
				UserVO user = serv.checkUserWithSessionKey(loginCookie.getValue());
				if(user != null){
					sess.setAttribute("login", user);
					return true;
				}
			}
			
			response.sendRedirect("/login");
			return false;
		}
		return true;
	}
	
	private void saveDest(HttpServletRequest req){
		String uri = req.getRequestURI();
		String query = req.getQueryString();
		if(query==null || query.equals("null")) query = "";
		else query = "?"+query;
		
		if(req.getMethod().equals("GET")) req.getSession().setAttribute("dest", uri+query);
	}
}
