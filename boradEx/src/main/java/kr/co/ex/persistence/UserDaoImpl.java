package kr.co.ex.persistence;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.ex.domain.UserVO;

@Repository
public class UserDaoImpl implements UserDao{
	
	@Autowired
	SqlSession sess;
	
	public final String namespace = "kr.co.ex.mapper.UserMapper";
	
	@Override
	public void create(UserVO vo) throws Exception {
		sess.insert(namespace+".create", vo);
	}
	
	@Override
	public void updatePoint(String uid, int point) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("point", point);
		sess.update(namespace+".updatePoint", map);
	}

}
