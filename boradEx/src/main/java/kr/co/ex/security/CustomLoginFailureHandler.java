package kr.co.ex.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.extern.log4j.Log4j;

@Log4j
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest arg0, HttpServletResponse arg1, AuthenticationException arg2)
			throws IOException, ServletException {
		
		arg2.printStackTrace();
		arg1.sendRedirect("/board/login");
	}

}
