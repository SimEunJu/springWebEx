package kr.co.ex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.ex.domain.MsgVO;
import kr.co.ex.persistence.MsgDao;
import kr.co.ex.persistence.UserDao;

@Service
public class MsgServiceImpl implements MsgService {
	
	@Autowired
	MsgDao msgdao;
	
	@Autowired
	UserDao userdao;
	
	@Override
	public void sendMsg(MsgVO msg) throws Exception {
		msgdao.create(msg);
		userdao.updatePoint(msg.getSender(), 10);
	}

	@Override
	public MsgVO readMsg(String uid, int mid) throws Exception {
		msgdao.updateState(mid);
		userdao.updatePoint(uid, 5);
		return msgdao.read(mid);
	}

}
