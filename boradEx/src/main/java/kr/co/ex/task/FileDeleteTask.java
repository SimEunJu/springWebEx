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
	
		// 1. db에서 한 폴더내의 파일 가져옴 이름 순으로
		// 2. 한 폴더내의 파일 이름 순으로 정렬
		// 3. row별로 파일 인덱스에 마크
		// 4. 마크 안 된 것 삭제
	
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
		
		// 한 폴더에 저장할 수 있는 최대 갯수 제한
		// 한 파일당 최대 크기 측정
		// 한 파일에 들어갈 수 잇는 최대 크기 초과하면 다음폴더애 저장
		private class CustomResultHandler implements ResultHandler<AttachVO>{
			@Override
			public void handleResult(ResultContext<? extends AttachVO> resultContext) {
				AttachVO vo = resultContext.getResultObject();
				
				
			}
			
		}
}
