package kr.co.ex.domain;

import java.util.Date;

import lombok.Data;

@Data
public class ReplyVO {
	private Integer rno;
	private Integer bno;
	private String reply;
	private String replyer;
	private Date regdate;
	private Date updatedate;

	private Integer parRno;
	private String password;
	private boolean secret;
	private char deleteType;
	
	private int addedCount;
	
	public boolean getSecret(){
		return secret;
	}
}
