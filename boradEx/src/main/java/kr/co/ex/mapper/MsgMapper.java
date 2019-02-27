package kr.co.ex.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ex.domain.MsgVO;

@Mapper
public interface MsgMapper {
	public List<MsgVO> readMsg(String receiver) throws Exception;
	public void updateSenderDeleteFlag(int msgNo) throws Exception;
	public void updateReceiverDeleteFlag(int msgNo) throws Exception;
	
	public void createMsg(MsgVO vo) throws Exception;
	public void createMsgList(List<String> receivers, MsgVO vo) throws Exception;
}
