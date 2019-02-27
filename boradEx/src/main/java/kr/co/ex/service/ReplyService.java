package kr.co.ex.service;

import java.util.List;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.dto.ReplyDto;

public interface ReplyService {
	public void addReply(ReplyVO vo) throws Exception;
	public List<ReplyVO> listReply(Integer bno) throws Exception;
	public List<ReplyDto> listCriteriaReply(Integer bno, Criteria cri, String currentUser) throws Exception;
	public List<ReplyVO> listCriteriaAddedReply(int parRno, Criteria cri) throws Exception;
	
	public int getTotalCount(Integer bno) throws Exception;
	public int getAddedTotalCount(int parRno) throws Exception;
	public int getTotalCntByReplyer(String username) throws Exception;
	
	public String getReplyer(int parBno) throws Exception;
	public ReplyVO getReply(int rno) throws Exception; 
	public void modifyReply(ReplyVO vo) throws Exception;
	public void removeReply(Integer rno) throws Exception;
}
