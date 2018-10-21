package kr.co.ex.service;

import kr.co.ex.domain.MsgVO;

public interface MsgService {
	public void sendMsg(MsgVO msg) throws Exception;
	public MsgVO readMsg(String uid, int mid) throws Exception;
}
