package kr.co.ex.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kr.co.ex.domain.AuthVO;
import kr.co.ex.domain.MemberVO;
import kr.co.ex.mapper.MemberMapper;
import kr.co.ex.security.domain.CustomUser;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
public class CustomUserDetailsService implements UserDetailsService {

	@Setter(onMethod_ = {@Autowired})
	private MemberMapper mapper;
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO user = mapper.read(username);	
		log.info(user);
		return new CustomUser(user);
	}

}
