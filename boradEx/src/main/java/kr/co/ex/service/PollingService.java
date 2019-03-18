package kr.co.ex.service;

import kr.co.ex.dto.PollingMsgDto;
import kr.co.ex.dto.PollingNotiDto;

public interface PollingService {
	public PollingNotiDto getNotiCnt(PollingNotiDto dto);
	public PollingMsgDto getMsgCnt(PollingMsgDto dto);
}
