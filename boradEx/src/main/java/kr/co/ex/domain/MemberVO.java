package kr.co.ex.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class MemberVO implements Serializable{

	private String userpw;
	private String username;
	private String state;
	private int reportCnt;
	
	private LocalDateTime regdate;
	private LocalDateTime updatedate;
	private LocalDateTime leaveDate; 
	
	private List<AuthVO> authList;
}
