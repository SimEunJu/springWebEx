package kr.co.ex.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.NotificationVO;
import kr.co.ex.dto.NotificationDto;
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
	@Transactional
	public List<NotificationDto> getNotifications(String username, Criteria cri) throws Exception {
		return notiMapper.readNotification(username, cri);
	}

	@Override
	public void markReadFlagNotification(int nno) throws Exception {
		notiMapper.markNotification(nno);
	}

	@Override
	public void deleteNotification(int nno) throws Exception {
		notiMapper.deleteNotification(nno);
	}

	@Override
	public int getNotiCntByUsername(String username) throws Exception {
		return notiMapper.readMemberNotiCnt(username);
	}

}
