package kr.co.ex.service;

import kr.co.ex.domain.NotificationVO;

public interface NotificationService {
	public void registerNotification(NotificationVO vo) throws Exception;
	public NotificationVO getNotification(String username) throws Exception;
}
