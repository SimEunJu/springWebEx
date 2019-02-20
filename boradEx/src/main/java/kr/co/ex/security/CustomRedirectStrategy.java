package kr.co.ex.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;

public class CustomRedirectStrategy extends DefaultRedirectStrategy {

	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		
		super.sendRedirect(request, response, url);
		response.sendRedirect("/board/login/oauth");
	}
	
}
