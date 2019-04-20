package kr.co.ex.security.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleOAuth2User implements OAuth2User, Serializable {

	private List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
	private Map<String, Object> attributes;
	private String name;	
	private String email;
	private String username;
	
	public GoogleOAuth2User(String email){
		this.name = email;
		this.email = email;
		this.username = email;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, Object> getAttributes() {
		if (this.attributes == null) {
            this.attributes = new HashMap<>();
            this.attributes.put("name", this.getName());  
            this.attributes.put("username", this.getUsername());
            this.attributes.put("email", this.getEmail());
        }
        return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

}
