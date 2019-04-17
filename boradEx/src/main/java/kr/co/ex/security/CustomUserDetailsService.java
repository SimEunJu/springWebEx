package kr.co.ex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kr.co.ex.domain.MemberVO;
import kr.co.ex.mapper.MemberMapper;
import kr.co.ex.security.domain.CustomUser;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@NoArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired private MemberMapper mapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO user = mapper.read(username);
		log.info(user.toString());
		return new CustomUser(user);
	}

}
