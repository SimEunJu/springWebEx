package kr.co.ex.filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/security-context.xml",
	"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
public class Oauth2FilterTest {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private Filter springSecurityFilterChain;

  private MockMvc mvc;

  @Before
  public void setup() {
      mvc = MockMvcBuilders
              .webAppContextSetup(context)
              .addFilters(springSecurityFilterChain)
              .build();
  }
  
  @Test
  public void testOauth(){
	  try {
		mvc
		    .perform(get("/").with(httpBasic("user","password")));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

}