package kr.co.ex.service;

import kr.co.ex.dto.PollingMsgDto;
import kr.co.ex.dto.PollingNotiDto;
import kr.co.ex.mapper.PollingMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PollingServiceImpl implements PollingService {

	private PollingMapper pollMapper;
	
	@Override
	public PollingNotiDto getNotiCnt(PollingNotiDto dto) {
		return pollMapper.readNotiCnt(dto);
	}

	@Override
	public PollingMsgDto getMsgCnt(PollingMsgDto dto) {
		return pollMapper.readMsgCnt(dto);
	}

}
