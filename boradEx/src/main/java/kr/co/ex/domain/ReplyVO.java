package kr.co.ex.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReplyVO {
	private Integer rno;
	private Integer bno;
	private String reply;
	private String replyer;
	private LocalDateTime regdate;
	private LocalDateTime updatedate;

	private Integer parRno;
	private String password;
	private boolean secret;
	private char deleteType;
	
	private int addedCount;
	
	public boolean getSecret(){
		return secret;
	}
}
