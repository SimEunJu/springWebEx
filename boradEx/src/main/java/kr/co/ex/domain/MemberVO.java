package kr.co.ex.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MemberVO implements Serializable{

	private String userid;
	private String userpw;
	private String username;
	private char enabled;
	
	private Date regdate;
	private Date updatedate;
	private List<AuthVO> authList;
}
