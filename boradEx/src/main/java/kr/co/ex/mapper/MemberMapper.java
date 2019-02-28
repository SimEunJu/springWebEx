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
	
	public List<MemberVO> listMember(@Param("page") int page, @Param("perPageNum") int perPageNum);
	public List<MemberVO> listReportMember(@Param("page") int start, @Param("perPageNum") int perPageNum);
	public List<MemberVO> listStateMember(@Param("page") int start, @Param("perPageNum") int perPageNum, @Param("state") String state);

	public void updateState(@Param("members") List<String> members, @Param("state") String state);

	public List<MemberVO> readUserByKeyword(String keyword);
}
