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
	
		// 1. db���� �Ϸ� ���� ����� ���� ��� �ҷ�����
		// 2. resultHandler�� ����ؼ� ó��
		// 3. db�� ������� ���� �͸� ��󳻴���/ ���� ���� ��� ���� �̸��� ��� -> �� -> ���� loop 3��
	
		@Scheduled(cron="* * * 4 * *")
		public void deleteFiles(){
			// �� ������ �Ϸ� ���� ������ ��� ������ �����Ѵٰ� ���� + �� ����
			
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
