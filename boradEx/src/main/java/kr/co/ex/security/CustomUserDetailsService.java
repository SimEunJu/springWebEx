package kr.co.ex.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kr.co.ex.domain.MemberVO;
import kr.co.ex.mapper.MemberMapper;
import kr.co.ex.security.domain.CustomUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	@NonNull private MemberMapper mapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO user = mapper.read(username);
		return new CustomUser(user);
	}

}
