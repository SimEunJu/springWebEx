package kr.co.ex.service;

import java.util.List;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;

public interface ReplyService {
	public void addReply(ReplyVO vo) throws Exception;
	public List<ReplyVO> listReply(Integer bno) throws Exception;
	public List<ReplyVO> listCriteriaReply(Integer bno, Criteria cri, String currentUser) throws Exception;
	public List<ReplyVO> listCriteriaAddedReply(int parRno, Criteria cri);
	
	public int getTotalCount(Integer bno) throws Exception;
	public int getAddedTotalCount(int parRno);
	
	public ReplyVO getReply(int rno) throws Exception; 
	public void modifyReply(ReplyVO vo) throws Exception;
	public void removeReply(Integer rno) throws Exception;
}
