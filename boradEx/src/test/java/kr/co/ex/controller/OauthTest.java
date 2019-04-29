package kr.co.ex.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
	"file:src/main/webapp/WEB-INF/spring/security-context.xml"})
@Log4j2
public class OauthTest {
	
	@Setter(onMethod_ = {@Autowired})
	private WebApplicationContext ctx;
	
	private MockMvc mockMvc;
	
	private static final String ID = "576530370417-8pa99jec06r4o6m4noonp3bui7t9rjpn.apps.googleusercontent.com";
	private static final String SECRET = "xzGKkwgvps7zqd0hU1-mjSxF";

	
	@Before
	public void setup(){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	@Test
	public void obtainAccessToken() throws Exception {

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("redirect_uri", "https://localhost:8443/board/list");
    params.add("client_id", ID); 
    params.add("scope", "email"); 
    params.add("response_type", "token");
    log.info(params.toString());
    
    ResultActions result =  mockMvc.perform(MockMvcRequestBuilders.get("/board/login/oauth/google"));
    log.info(result.toString());
    	
    String resultString = result.andReturn().getResponse().getContentAsString();

    //JacksonJsonParser jsonParser = new JacksonJsonParser();
    
    //log.info(jsonParser.parseMap(resultString).toString());
	}
}
