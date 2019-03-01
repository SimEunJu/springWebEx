package kr.co.ex.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.ex.domain.MsgVO;

@Mapper
public interface MsgMapper {
	public List<MsgVO> readMsg(String receiver) throws Exception;
	public void updateSenderDeleteFlag(int msgNo) throws Exception;
	public void updateReceiverDeleteFlag(int msgNo) throws Exception;
	
	public void createMsg(MsgVO vo) throws Exception;
	public void createMsgList(@Param("receivers") List<String> receivers, @Param("msg") MsgVO msg) throws Exception;
}
