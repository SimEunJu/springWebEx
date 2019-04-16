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
	
	// �ֱ� �������� 1���� ���� ȸ���� �޸�ȸ������ ���� ����
	private void changeUserStatToSleep(){
		LocalDateTime aYearAgo = LocalDateTime.of(LocalDate.now().minusYears(1), LocalTime.MIDNIGHT);
		long epochSecond = aYearAgo.toEpochSecond(ZoneOffset.ofHours(+9));
		sess.insert(createSleepMemView, epochSecond);
		sess.update(updateUserToSleep);
	}
	
	// �Ϸ� �������� ���� �Ͻ� ������Ʈ
	private void updateLastAccessTime(){
		LocalDateTime aDayAgo = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT);
		long epochSecond = aDayAgo.toEpochSecond(ZoneOffset.ofHours(+9));
		memServ.updateAccessTime(epochSecond);
	}
	
	// row handler�� ����ؾ� �ϳ�?
	private void sendStatChangeMail(){
		List<String> sleepList = sess.selectList(listSleepMember);
		List<MailDto> mailes = sleepList.stream().map(m -> {
			MailDto dto = new MailDto();
			dto.setTo(m);
			dto.setContent("�ȳ��ϼ���. "+m+" ��.\n1�� ���� [������ �Ϸ縦 �����]�� �湮�Ͻ� ������ ���� �޸�ȸ������ ��ȯ���� �˷��帳�ϴ�.");
			dto.setTitle("[������ �Ϸ縦 �����]���� �޸�ȸ�� �ȳ��帳�ϴ�.");
			dto.setFrom(GOOGLE_EMAIL_SENDER);
			return dto;
		}).collect(Collectors.toList());
		mailServ.send(mailes);
	}

}
