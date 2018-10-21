package kr.co.ex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.persistence.ReplyDao;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	ReplyDao dao;
	
	@Override
	public void addReply(ReplyVO vo) throws Exception {
		dao.create(vo);
	}

	@Override
	public List<ReplyVO> listReply(Integer bno) throws Exception {	
		return dao.list(bno);
	}

	@Override
	public List<ReplyVO> listCriteriaReply(Integer bno, Criteria cri) throws Exception {
		return dao.listCriteria(bno, cri);
	}
	
	@Override
	public int getTotalCount(Integer bno) throws Exception {
		return dao.totalCount(bno);
	}

	@Override
	public void modifyReply(ReplyVO vo) throws Exception {
		dao.update(vo);
	}

	@Override
	public void removeReply(Integer rno) throws Exception {
		dao.delete(rno);
	}
	
}
