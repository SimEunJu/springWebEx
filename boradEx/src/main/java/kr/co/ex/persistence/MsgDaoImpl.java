package kr.co.ex.persistence;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.ex.domain.MsgVO;

@Repository
public class MsgDaoImpl implements MsgDao {

	@Autowired
	SqlSession sess;
	
	public static final String namespace = "kr.co.ex.mapper.MsgMapper";
	
	@Override
	public MsgVO read(Integer mid) throws Exception {
		return (MsgVO) sess.selectList(namespace+".read", mid);
	}

	@Override
	public void create(MsgVO vo) throws Exception {
		sess.insert(namespace+".create", vo);
	}

	@Override
	public void updateState(Integer mid) throws Exception {
		sess.update(namespace, mid);
	}

}
