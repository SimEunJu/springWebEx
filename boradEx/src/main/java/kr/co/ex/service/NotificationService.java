package kr.co.ex.service;

import java.util.List;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.NotificationVO;
import kr.co.ex.dto.NotificationDto;

public interface NotificationService {
	public void registerNotification(NotificationVO vo) throws Exception;
	
	public List<NotificationDto> getNotifications(String username, Criteria cri) throws Exception;
	public int getNotiCntByUsername(String username) throws Exception;
	
	public void markReadFlagNotification(int nno) throws Exception;
	public void deleteNotification(int nno) throws Exception;
	public void deleteNotifications(List<Integer> notiNo) throws Exception;
	
}
