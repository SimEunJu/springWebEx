package kr.co.ex.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.NotificationVO;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.dto.NotificationDto;
import kr.co.ex.mapper.BoardMapper;
import kr.co.ex.mapper.NotificationMapper;
import kr.co.ex.mapper.ReplyMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	@NonNull private NotificationMapper notiMapper;
	@NonNull private ReplyMapper replyMapper;
	@NonNull private BoardMapper boardMapper;
	
	@Override
	public void registerNotification(ReplyVO vo, int bno) throws Exception {
		String writer = boardMapper.readWriterName(bno);
	
		// 게시글 작성자 != 댓글 작성자
		// 게시글 작성자에 알림 추가
		if(!writer.equals(vo.getReplyer())){
			NotificationVO replyNoti = NotificationVO.builder()
					.bno(bno)
					.username(writer)
					.build();
			notiMapper.createNoti(replyNoti);
			// 대댓글 작성자 != 댓글 작성자
			// 댓글 작성자에게 알림 추가
			if(vo.getParRno() != null){
				String parentReplyer = replyMapper.readReplyer(vo.getParRno());
				if(!writer.equals(writer) && !vo.getReplyer().equals(parentReplyer)){
					NotificationVO addedReplyNoti = NotificationVO.builder()
							.bno(bno)
							.rno(vo.getParRno())
							.username(parentReplyer)
							.build();
					notiMapper.createNoti(addedReplyNoti);
				}
			}
		}
	}

	@Override
	@Transactional
	public List<NotificationDto> getNotifications(String username, Criteria cri) throws Exception {
		return notiMapper.listNoti(username, cri);
	}

	@Override
	public void markReadFlagNotification(int nno) throws Exception {
		notiMapper.updateReadNoti(nno);
	}

	@Override
	public void deleteNotification(int nno) throws Exception {
		notiMapper.deleteNoti(nno);
	}

	@Override
	public void deleteNotifications(List<Integer> notiNo) throws Exception {
		notiMapper.deleteNoties(notiNo);
	}

	@Override
	public int getNotiCntByUsername(String username) throws Exception {
		return notiMapper.readMemberNotiCnt(username);
	}
}
