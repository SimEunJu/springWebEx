package kr.co.ex.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.domain.AuthVO;
import kr.co.ex.dto.LoginDto;
import kr.co.ex.mapper.MemberMapper;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class LoginServiceImpl implements LoginService {

	@Autowired
	MemberMapper mapper;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Override
	@Transactional
	public void signIn(LoginDto dto) {
		String encodedPw = encoder.encode(dto.getPassword());
		dto.setPassword(encodedPw);
		List<AuthVO> auth = new ArrayList<>();
		auth.add(new AuthVO(dto.getUsername(), "MEMBER"));
		dto.setAuths(auth);
		log.info(dto.toString());
		mapper.createUser(dto);
		for(AuthVO a: dto.getAuths()){
			mapper.createAuth(a);
		}
	}

/*	@Override
	public UserVO login(LoginDto dto) throws Exception {	
		return dao.login(dto);
	}

	@Override
	public void keepLogin(String sessionId, Date next, String uid) throws Exception {
		dao.keepLogin(sessionId, next, uid);
	}

	@Override
	public UserVO checkUserWithSessionKey(String sessionId) throws Exception {
		return dao.checkUserWithSessionKey(sessionId);
	}
	*/
}
