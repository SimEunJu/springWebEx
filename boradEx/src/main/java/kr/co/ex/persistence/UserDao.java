package kr.co.ex.persistence;

import kr.co.ex.domain.UserVO;

public interface UserDao {
	public void create(UserVO vo) throws Exception;
	public void updatePoint(String uid, int point) throws Exception;	
}
