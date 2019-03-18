package kr.co.ex.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ex.dto.PollingMsgDto;
import kr.co.ex.dto.PollingNotiDto;

@Mapper
public interface PollingMapper {
	public PollingNotiDto readNotiCnt(PollingNotiDto dto);
	public PollingMsgDto readMsgCnt(PollingMsgDto dto);
}
