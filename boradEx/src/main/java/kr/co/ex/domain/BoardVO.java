package kr.co.ex.domain;

import java.time.LocalDateTime;
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
	private LocalDateTime regdate;
	private LocalDateTime updatedate;
	private Integer viewcnt;
	private Integer userLike;
	private char deleteType;
	
	private Integer replyCnt;
	private List<AttachVO> files;
}
