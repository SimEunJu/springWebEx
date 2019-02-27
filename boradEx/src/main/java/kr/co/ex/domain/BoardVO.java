package kr.co.ex.domain;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BoardVO {
	private Integer bno;
	private String title;
	private String content;
	private String writer;
	private String password;
	private Date regdate;
	private Date updatedate;
	private Integer viewcnt;
	private Integer userLike;
	private char deleteType;
	
	private Integer replyCnt;
	private List<AttachVO> files;
}
