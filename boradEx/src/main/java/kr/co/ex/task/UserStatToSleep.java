package kr.co.ex.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.ex.dto.MailDto;
import kr.co.ex.service.MailService;
import kr.co.ex.service.MemberService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class UserStatToSleep {
	
	@NonNull private MailService mailServ;
	@NonNull private MemberService memServ;
	@NonNull private SqlSession sess;
	
	private final String updateUserToSleep = "update tbl_member, sleep_member"
			+ "set state = 'S' where tbl_member.username = sleep_member.username";
	private final String listSleepMember = "select username, access_date from sleep_member where state='S'";
	private final String createSleepMemView = "create or replace view sleep_member as"
			+ "select * "
			+ "from (select username, access_date  from tbl_member where access_date >= #{date})";
	
	@Value("${mail.google.email}")
	private String GOOGLE_EMAIL_SENDER;
	
	@Scheduled(cron="0 0 2 * * *")
	public void changeUserSleepAndSendMail(){
		updateLastAccessTime();
		changeUserStatToSleep();
		sendStatChangeMail();
	}
	
	// 최근 접속일이 1년이 넘은 회원은 휴면회원으로 상태 변경
	private void changeUserStatToSleep(){
		LocalDateTime aYearAgo = LocalDateTime.of(LocalDate.now().minusYears(1), LocalTime.MIDNIGHT);
		long epochSecond = aYearAgo.toEpochSecond(ZoneOffset.ofHours(+9));
		sess.insert(createSleepMemView, epochSecond);
		sess.update(updateUserToSleep);
	}
	
	// 하루 기준으로 접속 일시 업데이트
	private void updateLastAccessTime(){
		LocalDateTime aDayAgo = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT);
		long epochSecond = aDayAgo.toEpochSecond(ZoneOffset.ofHours(+9));
		memServ.updateAccessTime(epochSecond);
	}
	
	// row handler를 사용해야 하나?
	private void sendStatChangeMail(){
		List<String> sleepList = sess.selectList(listSleepMember);
		List<MailDto> mailes = sleepList.stream().map(m -> {
			MailDto dto = new MailDto();
			dto.setTo(m);
			dto.setContent("안녕하세요. "+m+" 님.\n1년 동안 [오늘의 하루를 들려줘]에 방문하신 내역이 없어 휴면회원으로 전환됨을 알려드립니다.");
			dto.setTitle("[오늘의 하루를 들려줘]에서 휴면회원 안내드립니다.");
			dto.setFrom(GOOGLE_EMAIL_SENDER);
			return dto;
		}).collect(Collectors.toList());
		mailServ.send(mailes);
	}

}
