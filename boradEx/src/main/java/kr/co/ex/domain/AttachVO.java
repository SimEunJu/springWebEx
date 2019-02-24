package kr.co.ex.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class AttachVO {
	private String fileName;
	private String uploadPath;
	private String uuid;
	private String fileType;
	
	private int bno;
}
