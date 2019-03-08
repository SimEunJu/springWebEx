package kr.co.ex.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.TreeSet;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.springframework.scheduling.annotation.Scheduled;

import kr.co.ex.util.DateUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

//@Component
@Log4j
//@RequiredArgsConstructor
public class FileDeleteTask {

		@NonNull
		private SqlSession sess;
		@NonNull
		private String uploadPath;
		
		private TreeSet<String> fileTree = new TreeSet<>();
	
		// 1. db에서 하루 동안 저장된 파일 목록 불러오기
		// 2. resultHandler를 사용해서 처리
		// 3. db에 저장되지 않은 것만 골라내느냐/ 폴더 내의 모든 파일 이름의 목록 -> 비교 -> 삭제 loop 3번
	
		@Scheduled(cron="* * * 4 * *")
		public void deleteFiles(){
			// 한 폴더에 하루 동안 생성된 모든 파일을 저장한다고 가정 + 양 많음
			
			LocalDateTime yesterday = DateUtils.getADaysAgo(1);
			String yesterdayPath = yesterday.format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", File.pathSeparator);
			
			HashMap<String, Object> map = new HashMap<>();
			map.put("yesterday", yesterday);
			map.put("uploadPath", Paths.get(uploadPath, yesterdayPath));
			sess.select("select uuid, file_type from tbl_attach where regdate>=#{yesterday} and upload_path=#{uploadPath}", map, new CustomResultHandler());
			
			String filePath = uploadPath + yesterdayPath;
			String files[] = Paths.get(filePath).toFile().list();
			for(String file : files){
				String fileName;
				
				if(file.startsWith("s_")) fileName = file.substring(2);
				else fileName = file;
				
				fileName = fileName.substring(0, fileName.lastIndexOf("_"));
				if(!fileTree.contains(fileName))
					try {
						Files.deleteIfExists(Paths.get(filePath, file));
					} catch (IOException e) {
						e.printStackTrace();
					} 
			}
		}
		
		private class CustomResultHandler implements ResultHandler<String>{
			@Override
			public void handleResult(ResultContext<? extends String> resultContext) {
				fileTree.add(resultContext.getResultObject());
			}
		}
}
