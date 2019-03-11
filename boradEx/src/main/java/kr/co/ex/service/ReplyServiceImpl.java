package kr.co.ex.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.dto.ReplyDto;
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
	public List<ReplyDto> listCriteriaReply(Integer bno, Criteria cri, String currentUser) throws Exception {

		if(getWriterName(bno).equals(currentUser)){
			return replyMapper.listCriteria(bno, cri)
					.stream().map(r -> {
						try {
							r.setAddedCount(getAddedTotalCount(r.getRno()));
							r.setSecret(false);
							// null°ª check·Î ¹Ù²ã¾ß
							if(!"0".equals(r.getDeleteType())) r.setDeleteFlag(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return r;
					}).collect(Collectors.toList());
		}
			
		
		return replyMapper.listCriteria(bno, cri).stream().map(r -> {
			if(r.isSecret()){
				r.setReplyer(null);
				r.setReply(null);
			}
			if(!"0".equals(r.getDeleteType())) r.setDeleteFlag(true);
			try {
				r.setAddedCount(getAddedTotalCount(r.getRno()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return r;
		}).collect(Collectors.toList());
	}
	
	@Override
	public List<ReplyVO> listReplyByWriter(String replyer) throws Exception {
		return replyMapper.listReplyByReplyer(replyer);
	}

	private String getWriterName(int bno){
		return boardMapper.readWriterName(bno);
	}
	
	@Override
	public String getReplyer(int parBno) throws Exception {
		return boardMapper.readWriterName(parBno);
	}

	@Override
	public List<ReplyVO> listCriteriaAddedReply(int parRno, Criteria cri) throws Exception {
		return replyMapper.listCriteriaAdded(parRno, cri);
	}

	@Override
	public int getTotalCount(int bno, boolean notIncludeAdded) throws Exception {
		return replyMapper.totalCount(bno, notIncludeAdded);
	}

	@Override
	public int getAddedTotalCount(int parRno) throws Exception {
		return replyMapper.addedTotalCount(parRno);
	}

	@Override
	public int getTotalCntByReplyer(String username) throws Exception {
		return replyMapper.readTotalCntByReplyer(username);
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
	@Transactional
	public void removeReply(String curUser, int rno, int bno) throws Exception {
		if(curUser.equals(getWriterName(bno))) replyMapper.delete("B", rno);
		else if(curUser.equals(getReplyer(rno))) replyMapper.delete("R", rno);
		else if(curUser.equals("ADMIN")) replyMapper.delete("A", rno);
	}

	
	@Override
	public void removeReplies(String deleteType, List<Integer> rno) throws Exception {
		replyMapper.deleteReplies(deleteType, rno);
	}

	@Override
	public void reportReply(int rno) throws Exception {
		replyMapper.updateReport(rno);
	}

}
