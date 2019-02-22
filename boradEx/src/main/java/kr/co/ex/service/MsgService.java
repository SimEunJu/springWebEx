package kr.co.ex.service;

import java.util.List;

import kr.co.ex.domain.MsgVO;

public interface MsgService {
	public List<MsgVO> getMsgList(String receiver) throws Exception;
	public void setSenderDeleteFlag(int msgNo) throws Exception;
	public void setReceiverDeleteFlag(int msgNo) throws Exception;
	public void registerMsg(MsgVO vo) throws Exception;
}
