package kr.co.ex.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.MsgVO;

@Mapper
public interface MsgMapper {
	public List<MsgVO> readMsgList(@Param("receiver")String receiver, @Param("cri") Criteria cri) throws Exception;
	public MsgVO readMsg(int msgNo) throws Exception;
	
	public void updateSenderDeleteFlag(@Param("msgNo")List<Integer> msgNo) throws Exception;
	public void updateReceiverDeleteFlag(@Param("msgNo")List<Integer> msgNo) throws Exception;
	
	public void createMsg(MsgVO vo) throws Exception;
	public void createMsgList(@Param("receivers") List<String> receivers, @Param("msg") MsgVO msg) throws Exception;
	
	public int getReceiverTotalCnt(String receiver) throws Exception;
	public int getSenderTotalCnt(String sender) throws Exception;
}
