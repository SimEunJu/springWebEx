package kr.co.ex.service;

import java.util.List;

import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.dto.ReplyDto;

public interface ReplyService {
	public void addReply(ReplyVO vo) throws Exception;
	public List<ReplyVO> listReply(Integer bno) throws Exception;
	public List<ReplyDto> listCriteriaReply(Integer bno, Criteria cri, String currentUser) throws Exception;
	public List<ReplyVO> listCriteriaAddedReply(int parRno, Criteria cri) throws Exception;
	public List<ReplyVO> listReplyByWriter(String replyer, Criteria cri) throws Exception;
	
	public int getTotalCount(int bno, boolean notIncludeAdded) throws Exception;
	public int getAddedTotalCount(int parRno) throws Exception;
	public int getTotalCntByReplyer(String username) throws Exception;
	
	public String getReplyer(int parBno) throws Exception;
	public ReplyVO getReply(int rno) throws Exception; 
	public void modifyReply(ReplyVO vo) throws Exception;
	
	public void removeReply(String deleteType, int rno, int bno) throws Exception;
	public void removeReplies(String deletType, List<Integer> rno) throws Exception;
	
	public void reportReply(int rno) throws Exception;
}
