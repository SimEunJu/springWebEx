package kr.co.ex.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.ex.domain.UserVO;
import kr.co.ex.dto.LoginDto;

@Repository
public class LoginDaoImpl implements LoginDao {
	
	@Autowired
	SqlSession sess;
	
	public static final String namespace = "kr.co.ex.mapper.LoginMapper";
	
	@Override
	public UserVO login(LoginDto dto) throws Exception {
		return sess.selectOne(namespace+".login", dto);
	}

	@Override
	public void keepLogin(String sessionId, Date next, String uid) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("sessionId", sessionId);
		map.put("next", next);
		map.put("uid", uid);
		sess.update(namespace+".keepLogin", map);
	}

	@Override
	public UserVO checkUserWithSessionKey(String sessionId) throws Exception {
		return sess.selectOne(namespace+".checkUserWithSessionKey", sessionId);
	}
	
}
