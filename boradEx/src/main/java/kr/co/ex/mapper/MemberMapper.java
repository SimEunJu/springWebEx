package kr.co.ex.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ex.domain.AuthVO;
import kr.co.ex.domain.MemberVO;
import kr.co.ex.dto.LoginDto;

@Mapper
public interface MemberMapper {
	public MemberVO read(String username);
	public void createUser(LoginDto login);
	public void createAuth(AuthVO auth);
}
