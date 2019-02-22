package kr.co.ex.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.ex.domain.MsgVO;
import kr.co.ex.mapper.MsgMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MsgServiceImpl implements MsgService {
	
	@NonNull
	private MsgMapper msgMapper;

	@Override
	public List<MsgVO> getMsgList(String receiver) throws Exception {
		return msgMapper.readMsg(receiver);
	}

	@Override
	public void setSenderDeleteFlag(int msgNo) throws Exception {
		msgMapper.updateSenderDeleteFlag(msgNo);
	}

	@Override
	public void setReceiverDeleteFlag(int msgNo) throws Exception {
		msgMapper.updateReceiverDeleteFlag(msgNo);
	}

	@Override
	public void registerMsg(MsgVO vo) throws Exception {
		msgMapper.createMsg(vo);
	}	
}
