package kr.co.ex.persistence;

import java.util.List;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;

public interface ReplyDao {
	public void create(ReplyVO vo) throws Exception;
	public List<ReplyVO> list(Integer bno) throws Exception;
	public List<ReplyVO> listCriteria(Integer bno, Criteria cri) throws Exception;
	public int totalCount(Integer bno) throws Exception;
	public void update(ReplyVO vo) throws Exception;
	public void delete(Integer rno) throws Exception;
	
}
