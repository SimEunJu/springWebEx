package kr.co.ex.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.log4j.Log4j;

@Log4j
public class UploadFileUtils {
	private static Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);
	
	
	public static String calcFolder(){
		String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		return date.replaceAll("-", File.pathSeparator);
	}
	
	public static String makePath(String path){
		File uploadPath = new File(path, calcFolder());
		
		if(!uploadPath.exists()) uploadPath.mkdirs();
		return uploadPath.toString();
	}
	
	public static boolean isImage(File file) throws IOException{
		String type = Files.probeContentType(file.toPath());
		log.info(type);
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
