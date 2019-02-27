package kr.co.ex.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.NotificationVO;
import kr.co.ex.dto.NotificationDto;

@Mapper
public interface NotificationMapper {
	public void createNotification(NotificationVO vo) throws Exception;
	
	public List<NotificationDto> readNotification(String username, Criteria cri) throws Exception;
	public int readMemberNotiCnt(String username) throws Exception;
	
	public void markNotification(int nno) throws Exception;
	public void deleteNotification(int nno) throws Exception;
}
