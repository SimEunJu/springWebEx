package kr.co.ex.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.ex.service.MemberService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class OauthInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired MemberService memServ;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		try{
			log.info("oauth");
			memServ.updateStateForLogin();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("pre oauth2");
		return true;
	}

}
