package kr.co.ex.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class ReplyDto {
	private Integer rno;
	private Integer bno;
	private String reply;
	private String replyer;
	private LocalDateTime regdate;
	private LocalDateTime updatedate;

	private Integer parRno;
	private boolean secret;
	private String deleteType;
	private String wrtier;
	
	private boolean deleteFlag;
	
	private int addedCount;
}
