package kr.co.ex.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BoardVO {
	private Integer bno;
	private String title;
	private String content;
	private String writer;
	private Date regdate;
	private Date updatedate;
	private Integer viewcnt;
	
	private List<AttachVO> files;
}
