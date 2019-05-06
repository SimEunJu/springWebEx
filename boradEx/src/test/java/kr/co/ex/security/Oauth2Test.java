package kr.co.ex.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/security-context.xml"
})
@Log4j2
public class Oauth2Test {
	
	@Autowired
	private ApplicationContext appContext;

	private MockMvc mvc;
	
    public static class MyFilter extends GenericFilterBean {
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
	
	@Test
	public void testOauthBeanExistence(){
		
		String beans[] = appContext.getBeanDefinitionNames();
		for(int i=0; i<beans.length; i++){
			log.info(beans[i]);
		}
		log.info(".................");
		SecurityContextHolder.
	}
}
