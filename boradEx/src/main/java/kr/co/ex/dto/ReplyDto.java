package kr.co.ex.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class ReplyDto {
	private Integer rno;
	private Integer bno;
	private String reply;
	private String replyer;
	private Date regdate;
	private Date updatedate;

	private Integer parRno;
	private boolean secret;
	private String deleteType;
	
	private boolean deleteFlag;
	
	private int addedCount;
}
