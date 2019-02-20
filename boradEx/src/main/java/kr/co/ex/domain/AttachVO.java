package kr.co.ex.domain;

import lombok.Data;

@Data
public class AttachVO {
	private String fileName;
	private String uploadPath;
	private String uuid;
	private boolean fileType;
	
	private int bno;
}
