package kr.co.ex.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.mapper.BoardMapper;
import kr.co.ex.mapper.ReplyMapper;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper replyMapper;
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Override
	public void addReply(ReplyVO vo) throws Exception {
		replyMapper.create(vo);
	}
	
	@Override
	public List<ReplyVO> listReply(Integer bno) throws Exception {	
		return replyMapper.list(bno).stream().map(r -> {
			if(r.getSecret()){
				r.setReplyer(null);
				r.setReply(null);
			}
			return r;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<ReplyVO> listCriteriaReply(Integer bno, Criteria cri, String currentUser) throws Exception {

		if(getWriterName(bno).equals(currentUser)){
			return replyMapper.listCriteria(bno, cri)
					.stream().map(r -> {
						r.setAddedCount(getAddedTotalCount(r.getRno()));
						return r;
					}).collect(Collectors.toList());
		}
			
		
		return replyMapper.listCriteria(bno, cri).stream().map(r -> {
			if(r.getSecret()){
				r.setReplyer(null);
				r.setReply(null);
			}
			r.setAddedCount(getAddedTotalCount(r.getRno()));
			return r;
		}).collect(Collectors.toList());
	}
	
	private String getWriterName(int bno){
		return boardMapper.readWriterName(bno);
	}
	
	@Override
	public List<ReplyVO> listCriteriaAddedReply(int parRno, Criteria cri) {
		return replyMapper.listCriteriaAdded(parRno, cri);
	}

	@Override
	public int getTotalCount(Integer bno) throws Exception {
		return replyMapper.totalCount(bno);
	}

	@Override
	public int getAddedTotalCount(int parRno) {
		return replyMapper.addedTotalCount(parRno);
	}

	@Override
	public ReplyVO getReply(int rno) throws Exception {
		return replyMapper.get(rno);
	}

	@Override
	public void modifyReply(ReplyVO vo) throws Exception {
		replyMapper.update(vo);
	}

	@Override
	public void removeReply(Integer rno) throws Exception {
		replyMapper.delete(rno);
	}
	
}
