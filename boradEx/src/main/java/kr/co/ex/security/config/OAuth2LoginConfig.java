package kr.co.ex.security.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import kr.co.ex.domain.MemberVO;
import kr.co.ex.security.domain.GoogleOAuth2User;
import kr.co.ex.service.MemberService;
import lombok.extern.log4j.Log4j;

@Log4j
@EnableWebSecurity
public class OAuth2LoginConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private MemberService memServ;
        
	private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauthUserService() {

		final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

		return (userRequest) -> {
			OAuth2User user = delegate.loadUser(userRequest);
			Map<String, Object> userInfo = user.getAttributes();
			String email = (String) userInfo.get("email");
			user = new GoogleOAuth2User(email, (String) email, (String) userInfo.get("name"));

			return user;
		};
	}

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository(
            OAuth2AuthorizedClientService authorizedClientService) {
        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
    }
    
    @Value("${oauth2.google.client.id}")
    private String clientId;
    
    @Value("${oauth2.google.client.secret}")
    private String clientSecret;  

    
    private ClientRegistration googleClientRegistration() {
    	
        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
            .clientId(clientId)
            .clientSecret(clientSecret)
            .scope("email profile")
            .redirectUriTemplate("{baseUrl}/board/oauth2/code/{registrationId}")
            .build();
    }
    
    @Autowired
    SpringSessionRememberMeServices rememberMeServ;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    	  		/*.requiresChannel()
    			.anyRequest()
    			.requiresSecure()
    			.and()*/
    		.formLogin()
    			.loginPage("/board/login")
    			.loginProcessingUrl("/login")
    			.and()
    		.rememberMe()
    			.rememberMeServices(rememberMeServ)
    			.and()
    		.logout()
    			.logoutUrl("board/logout")
    			.invalidateHttpSession(true)
    			.deleteCookies("SESSION")
    			.and()
    		.oauth2Login()
    			.loginPage("/board/oauth2/login")
    			.authorizationEndpoint()
    				.baseUri("/board/oauth2/authorization")
    				.and()
    			.redirectionEndpoint()
    				.baseUri("/board/oauth2/code/*")
    				.and()
    			.userInfoEndpoint()
    				.userService(oauthUserService());
        }
}