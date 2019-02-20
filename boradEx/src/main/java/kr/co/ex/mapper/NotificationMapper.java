package kr.co.ex.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ex.domain.NotificationVO;

@Mapper
public interface NotificationMapper {
	public void createNotification(NotificationVO vo) throws Exception;
	public NotificationVO readNotification(String username) throws Exception;
}
