package kr.co.ex.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BoardVO {
	
	private Integer bno;
	@NotNull private String title;
	@NotNull private String content;
    private String writer;
	private String password;
	private LocalDateTime regdate;
	private LocalDateTime updatedate;
	private Integer viewcnt;
	private Integer userLike;
	private String deleteType;
	private Integer reportCnt;
	
	private Integer replyCnt;
	private List<AttachVO> files;
}
