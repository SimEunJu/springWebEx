package kr.co.ex.dto;

import lombok.Data;

@Data
public class LoginDto {
	private String username;
	private String password;
	private boolean useCookie;	
}
