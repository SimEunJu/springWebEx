package kr.co.ex.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import kr.co.ex.util.UserType;
import lombok.Data;

@Data
public class MemberVO implements Serializable{

	private static final long serialVersionUID = -1328198072064647266L;

	private String userpw;
	private String username;
	private String state;
	private int reportCnt;
	
	private LocalDateTime regdate;
	private LocalDateTime updatedate;
	private LocalDateTime leaveDate; 
	
	private List<AuthVO> authList;
	
	public String getStateDescription(){
	switch (state) {
	case "A":
		return UserType.ACTIVE.getKorDescription();
	case "B":
		return UserType.BANNED.getKorDescription();
	case "S":
		return UserType.SLEEP.getKorDescription();
	case "L":
		return UserType.LEAVE.getKorDescription();
	case "R":
		return UserType.REPORT.getKorDescription();
	default:
		return null;
	}
}

}
