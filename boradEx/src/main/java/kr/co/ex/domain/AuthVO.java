package kr.co.ex.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthVO implements Serializable{

	private String username;
	private String auth;

	public AuthVO(String username, String auth) {
		this.username = username;
		this.auth = auth;
	}

	public AuthVO() {
	}
	
}
