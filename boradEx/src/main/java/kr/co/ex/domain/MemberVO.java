package kr.co.ex.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class MemberVO implements Serializable{

	private String userpw;
	private String username;
	private char enabled;
	
	private LocalDateTime regdate;
	private LocalDateTime updatedate;
	private List<AuthVO> authList;
}
