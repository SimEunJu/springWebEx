package kr.co.ex.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

public class UploadFileUtils {
	private static Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);
	
	public static String uploadFile(String uploadPath, String originalName, byte[] fileData) throws Exception{
		UUID uid = UUID.randomUUID();
		String savedName = uid.toString()+"_"+originalName;
		String savedPath = calcPath(uploadPath);
		File target = new File(uploadPath + savedPath + File.separator,savedName);
		FileCopyUtils.copy(fileData, target);
		String format = originalName.substring(originalName.indexOf(".")+1);
		String uploadedFileName;
		if(MediaUtil.getMediaType(format) != null){
			uploadedFileName = makeThumbnail(uploadPath, savedPath, savedName);
		}else{
			uploadedFileName = makeIcon(uploadPath, savedPath, savedName);
		}
		return uploadedFileName;
	}
	
	private static String makeIcon(String uploadpath, String path, String fileName){
		return (path + File.separator + fileName).replace(File.separator, "/");
	}
	
	private static String calcPath(String uploadPath){
		Calendar cal = Calendar.getInstance();
		String yearPath = File.separator + cal.get(Calendar.YEAR);
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);
		String dayPath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		makeDir(uploadPath, yearPath, monthPath, dayPath);
		return dayPath;
	}
	
	private static void makeDir(String uploadPath, String ...paths){
		if(new File(uploadPath + paths[paths.length-1]).exists()) return;
		
		for(String path : paths){
			File dirPath = new File(uploadPath + path);
			if(! dirPath.exists()) dirPath.mkdir();
		}
	}
	
	private static String makeThumbnail(String uploadpath, String path, String fileName) throws IOException{
		BufferedImage sourceImg = ImageIO.read(new File(uploadpath+path,fileName));
		BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);
		String thumbnailName = uploadpath + path + File.separator +"s_" + fileName;
		File thumbnail = new File(thumbnailName);
		String format = fileName.substring(fileName.lastIndexOf(".")+1);
		ImageIO.write(destImg, format.toUpperCase(), thumbnail);
		return thumbnailName.substring(uploadpath.length()).replace(File.separatorChar, '/');
	}
}
