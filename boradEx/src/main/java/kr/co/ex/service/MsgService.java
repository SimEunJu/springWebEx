package kr.co.ex.service;

import java.util.List;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.MsgVO;

public interface MsgService {
	public List<MsgVO> getMsgList(String receiver, Criteria cri) throws Exception;
	public MsgVO getMsg(int msgNo) throws Exception;
	
	public void setSenderDeleteFlag(List<Integer> msgNo) throws Exception;
	public void setReceiverDeleteFlag(List<Integer> msgNo) throws Exception;
	
	public void registerMsg(MsgVO vo) throws Exception;
	public void registerMsgList(List<String> receivers, MsgVO vo) throws Exception;
	
	public int getReceiverTotalCnt(String receiver) throws Exception;
	public int getSenderTotalCnt(String sender) throws Exception;
}
