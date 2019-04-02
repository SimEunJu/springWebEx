package kr.co.ex.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.ex.service.MemberService;
import lombok.extern.log4j.Log4j;

@Log4j
public class OauthInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired MemberService memServ;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try{
			memServ.updateStateForLogin();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
