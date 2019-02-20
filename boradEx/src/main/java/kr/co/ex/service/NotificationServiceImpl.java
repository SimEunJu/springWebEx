package kr.co.ex.service;

import org.springframework.stereotype.Service;

import kr.co.ex.domain.NotificationVO;
import kr.co.ex.mapper.NotificationMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	@NonNull
	private NotificationMapper notiMapper;
	
	@Override
	public void registerNotification(NotificationVO vo) throws Exception {
		notiMapper.createNotification(vo);
	}

	@Override
	public NotificationVO getNotification(String username) throws Exception {
		return notiMapper.readNotification(username);
	}

}
