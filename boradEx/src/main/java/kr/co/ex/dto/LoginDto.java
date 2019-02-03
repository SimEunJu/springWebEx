package kr.co.ex.dto;

import java.util.List;

import kr.co.ex.domain.AuthVO;
import lombok.Data;

@Data
public class LoginDto {
	private String username;
	private String password;
	private List<AuthVO> auths;
}
