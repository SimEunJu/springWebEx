package kr.co.ex.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.ToIntFunction;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Component
@Log4j
@AllArgsConstructor
public class FileDeleteTask {

		private SqlSession sess;
		private String uploadPath;
		private String yesterdayIso;
	
		// 1. db���� �� �������� ���� ������ �̸� ������
		// 2. �� �������� ���� �̸� ������ ����
		// 3. row���� ���� �ε����� ��ũ
		// 4. ��ũ �� �� �� ����
	
		@Scheduled(cron="* * * 4 * *")
		public void deleteFiles(){
			LocalDateTime yesterday = DateUtils.getADaysAgo(1);
			yesterdayIso = yesterday.format(DateTimeFormatter.ISO_LOCAL_DATE);
			
			String files[] = Paths.get(uploadPath, yesterdayIso).toFile().list();
			Arrays.sort(files);
			
			HashMap<String, String> map = new HashMap<>();
			map.put("yesterday", yesterdayIso);
			map.put("uploadPath", Paths.get(uploadPath, yesterdayIso).toString());
			sess.select("select * from tbl_attach where regdate>#{yesterdayIso} and upload_path=#{uploadPath}", map, new CustomResultHandler());
			
			
		}
		
		// �� ������ ������ �� �ִ� �ִ� ���� ����
		// �� ���ϴ� �ִ� ũ�� ����
		// �� ���Ͽ� �� �� �մ� �ִ� ũ�� �ʰ��ϸ� ���������� ����
		private class CustomResultHandler implements ResultHandler<AttachVO>{
			@Override
			public void handleResult(ResultContext<? extends AttachVO> resultContext) {
				AttachVO vo = resultContext.getResultObject();
				
				
			}
			
		}
}
