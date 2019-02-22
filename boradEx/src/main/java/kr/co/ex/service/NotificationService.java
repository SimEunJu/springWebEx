package kr.co.ex.service;

import java.util.List;

import kr.co.ex.domain.NotificationVO;
import kr.co.ex.dto.NotificationDto;

public interface NotificationService {
	public void registerNotification(NotificationVO vo) throws Exception;
	public List<NotificationDto> getNotifications(String username) throws Exception;
	public void markReadFlagNotification(int nno) throws Exception;
	public void deleteNotification(int nno) throws Exception;
}
