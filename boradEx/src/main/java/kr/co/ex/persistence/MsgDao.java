package kr.co.ex.persistence;

import kr.co.ex.domain.MsgVO;

public interface MsgDao {
	public MsgVO read(Integer mid) throws Exception;
	public void create(MsgVO vo) throws Exception;
	public void updateState(Integer mid) throws Exception;
}
