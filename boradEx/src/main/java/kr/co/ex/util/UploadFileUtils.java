package kr.co.ex.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class UploadFileUtils {
	private static Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);
	
	public static String calcFolder(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		return str.replaceAll("-", File.separator);
	}
	
	public static String makePath(String path){
		File uploadPath = new File(path, calcFolder());
		if(!uploadPath.exists()) uploadPath.mkdirs();
		return uploadPath.toString();
	}
	
	public static boolean isImage(File file) throws IOException{
		return Files.probeContentType(file.toPath()).startsWith("image");
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
