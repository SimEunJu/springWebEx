package kr.co.ex.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;

@Repository
public class ReplayDaoImpl implements ReplyDao {
	
	@Autowired
	private SqlSession sess;
	
	private static final String namespace = "kr.co.ex.mapper.ReplyMapper";
	
	@Override
	public void create(ReplyVO vo) throws Exception {
		sess.insert(namespace+".create", vo);
	}

	@Override
	public List<ReplyVO> list(Integer bno) throws Exception {
		return sess.selectList(namespace+".list", bno);
	}

	@Override
	public List<ReplyVO> listCriteria(Integer bno, Criteria cri) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("bno", bno);
		map.put("cri", cri);
		return sess.selectList(namespace+".listCriteria", map);
	}
	
	@Override
	public int totalCount(Integer bno) throws Exception {
		return sess.selectOne(namespace+".totalCount", bno);
	}

	@Override
	public void update(ReplyVO vo) throws Exception {
		sess.update(namespace+".update", vo);
	}

	@Override
	public void delete(Integer rno) throws Exception {
		sess.delete(namespace+".delete", rno);
	}

}
