package kr.co.ex.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.ex.domain.UserStatVO;
import kr.co.ex.mapper.AdminStatTaskMapper;
import kr.co.ex.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Component
@Log4j
@AllArgsConstructor
public class AdminStatTask {
	
	private AdminStatTaskMapper mapper;
	
	@Scheduled(cron="* * * 3 * *")
	public void makeUserStatForAdmin(){
		LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		UserStatVO vo = UserStatVO.builder()
				.today(DateUtils.getToday())
				.joinCnt(mapper.getJoinUserCount(today))
				.leaveCnt(mapper.getLeaveUserCount(today))
				.postCnt(mapper.getPostCount(today))
				.build();
		log.info(vo.toString());
		mapper.createUserStat(vo);
	}
}
