package kr.co.ex.dto;

import lombok.Data;

@Data
public class LoginDto {
	private String uid;
	private String upw;
	private boolean useCookie;
	
}
