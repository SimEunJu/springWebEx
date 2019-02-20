package kr.co.ex.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
			throws IOException, ServletException {
		List<String> roles = new ArrayList<>();
		auth.getAuthorities().forEach(authority -> {
			roles.add(authority.getAuthority());
		});
	/*	if(roles.contains("ADMIN")){
			res.sendRedirect("/admin");
			return;
		}
		if(roles.contains("MEMBER")){
			res.sendRedirect("/member");
			return;
		}*/
		res.sendRedirect("/");
	}

}
