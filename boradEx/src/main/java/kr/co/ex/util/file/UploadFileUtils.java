package kr.co.ex.util.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UploadFileUtils {
	
	// 운영체제별 다른 file separator
	public static String calcFolder(){
		String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		return date.replaceAll("-", "/");
	}
	
	public static String makePath(String path){
		File uploadPath = new File(path, calcFolder());
		
		if(!uploadPath.exists()) uploadPath.mkdirs();
		return uploadPath.toString();
	}
	
	public static boolean isImage(File file) throws IOException{
		String type = Files.probeContentType(file.toPath());
		if(type != null) return type.startsWith("image");
		return false;
	}
	
	public static void makeThumbnail(String path, String fileName) throws IOException{
		BufferedImage sourceImg = ImageIO.read(new File(path, fileName));
		BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);
		String thumbnailName = "s_" + fileName;
		File thumbnail = new File(path, thumbnailName);
		String format = fileName.substring(fileName.lastIndexOf(".")+1);
		ImageIO.write(destImg, format.toUpperCase(), thumbnail);
	}
}
