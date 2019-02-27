package kr.co.ex.service;

import java.util.List;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.MemberVO;
import kr.co.ex.util.UserType;

public interface MemberService {
	public int getMemberCnt();
	
	public List<MemberVO> ListCategorizedMember(UserType type, Criteria cri);
	public List<MemberVO> listMember(int start, int end);
	public List<MemberVO> listReportMember(int start, int end);
	public List<MemberVO> listStateMember(int start, int end, String state);
	
	public void updateState(List<String> members, String state);
}
