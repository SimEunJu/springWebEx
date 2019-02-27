package kr.co.ex.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.ex.domain.AuthVO;
import kr.co.ex.domain.MemberVO;
import kr.co.ex.dto.LoginDto;

@Mapper
public interface MemberMapper {
	
	public MemberVO read(String username);
	public int readMemberCnt();
	
	public void createUser(LoginDto login);
	public void createAuth(AuthVO auth);
	
	public List<MemberVO> listMember(@Param("start") int start, @Param("end") int end);
	public List<MemberVO> listReportMember(@Param("start") int start, @Param("end") int end);
	public List<MemberVO> listStateMember(@Param("start") int start, @Param("end") int end, @Param("state") String state);

	public void updateState(List<String> members, String state);
}
