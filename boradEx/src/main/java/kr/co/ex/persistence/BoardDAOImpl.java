package kr.co.ex.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.SearchCriteria;

@Repository
public class BoardDAOImpl implements BoardDAO {

	@Autowired
	private SqlSessionTemplate sess;
	private static final String namespace = "kr.co.ex.mapper.BoardMapper";
	
	@Override
	public void create(BoardVO vo) throws Exception {
		sess.insert(namespace+".create", vo);
	}

	@Override
	public BoardVO read(Integer bno) throws Exception {
		return sess.selectOne(namespace+".read", bno);
	}

	@Override
	public void update(BoardVO vo) throws Exception {
		sess.update(namespace+".update", vo);
	}

	@Override
	public void delete(Integer bno) throws Exception {
		sess.delete(namespace+".delete", bno);
	}

	@Override
	public List<BoardVO> listAll() throws Exception {
		return sess.selectList(namespace+".listAll");
	}

	@Override
	public List<BoardVO> listCriteria(Criteria cri) throws Exception {
		return sess.selectList(namespace+".listPage", cri);
	}

	@Override
	public int totalCount() throws Exception {
		return sess.selectOne(namespace+".totalCount");
	}

	@Override
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception {
		return sess.selectList(namespace+".listSearch", cri);
	}

	@Override
	public int searchCount(SearchCriteria cri) throws Exception {
		return sess.selectOne(namespace+".searchCount", cri);
	}

	@Override
	public void addAttach(String fullName) throws Exception {
		sess.insert(namespace+".addAttach", fullName);
	}

	@Override
	public List<String> getAttach(Integer bno) throws Exception {
		return sess.selectList(namespace+".getAttach", bno);
	}

	@Override
	public void deleteAttach(String fullName) throws Exception {
		sess.delete(namespace+".deleteAttach", fullName);
	}

	@Override
	public void deleteAllAttach(Integer bno) throws Exception {
		sess.delete(namespace+".deleteAllAttach", bno);
	}

	@Override
	public void replaceAttach(String fullName, Integer bno) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("fullname", fullName);
		map.put("bno", bno);
		sess.insert(namespace+".replaceAttach", map);
	}

	@Override
	public int readReplyCnt(int bno) throws Exception {
		return sess.selectOne(namespace+".readReplyCnt", bno);
	}

	@Override
	public void updateViewCnt(int bno) throws Exception {
		sess.update(namespace+".updateViewCnt", bno);
	}

}
