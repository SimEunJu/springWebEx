package kr.co.ex.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.ex.annoation.Loggable;
import kr.co.ex.domain.Criteria;
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
	public List<MsgVO> getMsgList(String receiver, Criteria cri) throws Exception {
		return msgMapper.readMsgList(receiver, cri);
	}
	
	@Override
	public MsgVO getMsg(int msgNo) throws Exception {
		return msgMapper.readMsg(msgNo);
	}	

	@Override
	public void setSenderDeleteFlag(List<Integer> msgNo) throws Exception {
		msgMapper.updateSenderDeleteFlag(msgNo);
	}

	@Override
	public void setReceiverDeleteFlag(List<Integer> msgNo) throws Exception {
		msgMapper.updateReceiverDeleteFlag(msgNo);
	}

	@Override
	public void setReceiverReadFlag(int msgNo) throws Exception {
		msgMapper.updateReceiverReadFlag(msgNo);
	}
	
	@Override
	@Loggable
	public void registerMsgList(List<String> receivers, MsgVO vo) throws Exception {
		msgMapper.createMsgList(receivers, vo);
	}

	@Override
	@Loggable
	public void registerMsg(MsgVO vo) throws Exception {
		msgMapper.createMsg(vo);
	}

	@Override
	public int getReceiverTotalCnt(String receiver) throws Exception {
		return msgMapper.getReceiverTotalCnt(receiver);
	}

	@Override
	public int getSenderTotalCnt(String sender) throws Exception {
		return msgMapper.getSenderTotalCnt(sender);
	}
	
}
