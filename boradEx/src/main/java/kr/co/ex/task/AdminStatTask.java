package kr.co.ex.task;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.ex.domain.UserStatVO;
import kr.co.ex.mapper.AdminStatTaskMapper;
import kr.co.ex.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

//@Component
@Log4j
@AllArgsConstructor
public class AdminStatTask {
	
	private AdminStatTaskMapper mapper;
	
	// second/ minute/ hour/ day of month/ month/ day of week
	@Scheduled(cron="0 0 3 * * *")
	public void makeUserStatForAdmin(){
		LocalDateTime today = DateUtils.getToday();
		
		UserStatVO vo = UserStatVO.builder()
				.today(DateUtils.getToday())
				.joinCnt(mapper.getJoinUserCount(today))
				.leaveCnt(mapper.getLeaveUserCount(today))
				.postCnt(mapper.getPostCount(today))
				.visitCnt(mapper.getVisitCount(today.toEpochSecond(ZoneOffset.ofHours(+9))*1000))
				.build();
		log.info(vo.toString());
		
		mapper.createUserStat(vo);
		
	}
}
