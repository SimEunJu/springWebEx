package kr.co.ex.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
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
	private String deleteType;
}
