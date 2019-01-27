package kr.co.ex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.mapper.ReplyMapper;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper mapper;
	
	@Override
	public void addReply(ReplyVO vo) throws Exception {
		mapper.create(vo);
	}

	@Override
	public List<ReplyVO> listReply(Integer bno) throws Exception {	
		return mapper.list(bno);
	}

	@Override
	public List<ReplyVO> listCriteriaReply(Integer bno, Criteria cri) throws Exception {
		return mapper.listCriteria(bno, cri);
	}
	
	@Override
	public int getTotalCount(Integer bno) throws Exception {
		return mapper.totalCount(bno);
	}

	@Override
	public void modifyReply(ReplyVO vo) throws Exception {
		mapper.update(vo);
	}

	@Override
	public void removeReply(Integer rno) throws Exception {
		mapper.delete(rno);
	}
	
}
