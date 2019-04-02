package kr.co.ex.service;

import java.util.List;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.MemberVO;
import kr.co.ex.util.UserType;

public interface MemberService {
	public MemberVO getMember(String username);
	public int getMemberCnt();
	
	public List<MemberVO> ListCategorizedMember(UserType type, Criteria cri);
	public List<MemberVO> listMember(Criteria cri);
	public List<MemberVO> listReportMember(Criteria cri);
	public List<MemberVO> listStateMember(Criteria cri, UserType userType);
	
	public void updateState(List<String> members, UserType type);
	public void leave(String username) throws Exception;
	public void updateReportCnt(String username, int diff);
	public void updateAccessTime(long epochSecond);
	
	public void updateStateForLogin();
	
	public List<MemberVO> getMemberByKeyword(String keyword);
}
