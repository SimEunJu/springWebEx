package kr.co.ex.util.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import kr.co.ex.domain.AttachVO;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DeleteFileUtils {

	@Resource private static String uploadPath;
	
	public static void deleteFiles(List<AttachVO> attaches) throws IOException{
		if(attaches == null || attaches.size() == 0) return;
	
			attaches.forEach(attach -> {
				log.info(attach.toString());
				try{
					Path file = Paths.get(uploadPath+attach.getUploadPath()+"/"+attach.getUuid()+"_"+attach.getFileName());
					Files.deleteIfExists(file);
					if(attach.getFileType().startsWith("image")){
						Path thumbnail = Paths.get(uploadPath+attach.getUploadPath()+"/s_"+attach.getUuid()+"_"+attach.getFileName());
						Files.deleteIfExists(thumbnail);
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			});
	}
}
