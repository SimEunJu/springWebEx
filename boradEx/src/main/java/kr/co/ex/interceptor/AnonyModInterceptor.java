package kr.co.ex.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.log4j.Log4j;

@Log4j
public class AnonyModInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info(response.getStatus());
		if(response.getStatus() == 200){
			HttpSession sess = request.getSession();
			sess.setAttribute("anonyMod", true);
		}
	}
	/*
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
	*/
}
