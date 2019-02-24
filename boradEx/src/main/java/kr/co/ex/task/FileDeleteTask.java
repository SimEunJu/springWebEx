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
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Component
@Log4j
@AllArgsConstructor
public class FileDeleteTask {

		private SqlSession sess;
		private String uploadPath;
		private String yesterdayIso;
	
		@Scheduled(cron="* * * 4 * *")
		public void deleteFiles(){
			LocalDateTime yesterday = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).minusDays(1);
			yesterdayIso = yesterday.format(DateTimeFormatter.ISO_LOCAL_DATE);
			
			String files[] = Paths.get(uploadPath, yesterdayIso).toFile().list();
			ToIntFunction<String> parse  = (x)-> Integer.parseInt(x);
			int fileIdx[] = Arrays.stream(files).mapToInt(parse).toArray();
			Collections.sort(fileIdx);;
			
			for(int i=0; i<fileIdx.length; i++){
				HashMap<String, String> map = new HashMap<>();
				
				String filePath = yesterdayIso.replaceAll("-", File.separator)+File.separator+fileIdx[i];
				map.put("yesterday", yesterdayIso);
				map.put("uploadPath", filePath);
				sess.select("select * from tbl_attach where regdate>#{yesterdayIso} and upload_path=#{uploadPath}", map, new CustomResultHandler());
		
			}
			
		}
		// 한 폴더에 저장할 수 있는 최대 갯수 제한
		// 한 파일당 최대 크기 측정
		// 한 파일에 들어갈 수 잇는 최대 크기 초과하면 다음폴더애 저장
		private class CustomResultHandler implements ResultHandler<AttachVO>{
			@Override
			public void handleResult(ResultContext<? extends AttachVO> resultContext) {
				AttachVO vo = resultContext.getResultObject();
				Path file = Paths.get(uploadPath, yesterdayIso, vo.getUuid()+"_"+vo.getFileName());
				File dir = Paths.get(uploadPath, yesterdayIso).toFile();
				
			}
			
		}
}
