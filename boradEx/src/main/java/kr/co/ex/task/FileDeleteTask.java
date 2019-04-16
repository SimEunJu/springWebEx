package kr.co.ex.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.ibatis.session.SqlSession;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.ex.util.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Component
@Log4j
@RequiredArgsConstructor
public class FileDeleteTask {

		@NonNull private SqlSession sess;
		@NonNull private String uploadPath;

		@Scheduled(cron="0 0 4 * * *")
		public void deleteFiles(){
			// 한 폴더에 하루 동안 생성된 모든 파일을 저장한다고 가정 + 양 많음
			
			LocalDateTime yesterday = DateUtils.getADaysAgo(1);
			LocalDateTime todayMid = DateUtils.getADaysAgo(0);
			String yesterdayPath = yesterday.format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", File.pathSeparator);
			
			String filePath = uploadPath + yesterdayPath;
			try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(filePath))){
				for(Path entry : stream){
					String fileName = entry.getFileName().toString();
					if(fileName.startsWith("s_")) continue;
					
					String uuid = fileName.substring(0, fileName.lastIndexOf("_"));
					String fileType = sess.selectOne("select file_type from tbl_attach where uuid='"+uuid+"'");
					if(fileType != null) Files.delete(entry);
					if(fileType.contains("image")) Files.delete(Paths.get(filePath, "s_"+fileName));
				}
			} catch (IOException e) {
				log.error("file 삭제 cron io exception");
				log.error(e);
				e.printStackTrace();
			}
		}
}
