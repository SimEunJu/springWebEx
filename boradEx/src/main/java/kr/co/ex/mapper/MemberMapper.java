package kr.co.ex.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.ex.domain.AuthVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.MemberVO;
import kr.co.ex.dto.LoginDto;

@Mapper
public interface MemberMapper {
	
	public MemberVO read(String username);
	public int readMemberCnt();
	
	public void createUser(LoginDto login);
	public void createAuth(AuthVO auth);
	
	public List<MemberVO> listMember(Criteria cri);
	public List<MemberVO> listMemberByKeyword(String keyword);
	public List<MemberVO> listReportMember(Criteria cri);
	public List<MemberVO> listStateMember(@Param("cri") Criteria cri, @Param("state") String state);

	public void updateState(@Param("members") List<String> members, @Param("state") String state);
	public void updateLeave(String username) throws Exception;
	public void updateReportCnt(@Param("username") String username, @Param("diff") int diff);
	public void updateAccessTimeProcedure(long epochSecond);
	
	public void updateStateForLogin(String username);
}
