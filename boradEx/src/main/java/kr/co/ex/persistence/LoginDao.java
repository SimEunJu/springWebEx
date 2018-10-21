package kr.co.ex.persistence;

import java.util.Date;

import kr.co.ex.domain.UserVO;
import kr.co.ex.dto.LoginDto;

public interface LoginDao {
	public UserVO login(LoginDto dto) throws Exception;
	public void keepLogin(String sessionId, Date next, String uid) throws Exception;
	public UserVO checkUserWithSessionKey(String sessionId) throws Exception;

}
