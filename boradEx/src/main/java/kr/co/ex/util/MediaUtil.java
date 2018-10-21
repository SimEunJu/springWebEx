package kr.co.ex.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

public class MediaUtil {
	private static Map<String, MediaType> mediaMap;
	
	static {
		mediaMap = new HashMap<>();
		mediaMap.put("PNG", MediaType.IMAGE_PNG);
		mediaMap.put("GIF", MediaType.IMAGE_GIF);
		mediaMap.put("JPG", MediaType.IMAGE_JPEG);
	}
	
	public static MediaType getMediaType(String type){
		return mediaMap.get(type.toUpperCase());
	}
}
