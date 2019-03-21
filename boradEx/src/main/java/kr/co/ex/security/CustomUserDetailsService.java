package kr.co.ex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kr.co.ex.domain.MemberVO;
import kr.co.ex.mapper.MemberMapper;
import kr.co.ex.security.domain.CustomUser;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@NoArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired private MemberMapper mapper;
	@Autowired private BCryptPasswordEncoder encoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO user = mapper.read(username);
		return new CustomUser(user);
	}

}
