package kr.co.ex.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.domain.AuthVO;
import kr.co.ex.domain.UserVO;
import kr.co.ex.dto.LoginDto;
import kr.co.ex.mapper.MemberMapper;
import kr.co.ex.persistence.LoginDao;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	MemberMapper mapper;
	
	@Override
	@Transactional
	public void signIn(LoginDto dto) {
		mapper.createUser(dto);
		for(AuthVO auth: dto.getAuths()){
			mapper.createAuth(auth);
		}
	}

	@Autowired
	LoginDao dao;
	
	@Override
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
	
}
