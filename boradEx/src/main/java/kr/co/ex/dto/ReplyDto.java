package kr.co.ex.dto;

import kr.co.ex.domain.ReplyVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class ReplyDto extends ReplyVO{
	
	private String wrtier;
	private boolean deleteFlag;
	
	private int addedCount;
}
