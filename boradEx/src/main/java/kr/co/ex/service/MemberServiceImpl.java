package kr.co.ex.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.MemberVO;
import kr.co.ex.exception.UndefinedMemberType;
import kr.co.ex.mapper.MemberMapper;
import kr.co.ex.util.UserType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	@NonNull
	private MemberMapper memMapper;
	
	@Transactional
	@Override
	public List<MemberVO> ListCategorizedMember(UserType type, Criteria cri) throws UndefinedMemberType{
		switch (type) {
		case ALL: 
			return listMember(cri);
		case ACTIVE:
		case BANNED:
		case SLEEP:
		case LEAVE:
			return listStateMember(cri, type.getTypeInitial());
		case REPORT:
			return listReportMember(cri);
		default:
			throw new UndefinedMemberType(type.toString());
		}
	}
	
	@Override
	public List<MemberVO> listMember(Criteria cri) {
		return memMapper.listMember(cri);
	}

	@Override
	public List<MemberVO> listReportMember(Criteria cri) {
		return memMapper.listReportMember(cri);
	}

	@Override
	public List<MemberVO> listStateMember(Criteria cri, String state) {
		return memMapper.listStateMember(cri, state);
	}

	@Override
	public int getMemberCnt() {
		return memMapper.readMemberCnt();
	}

	@Override
	public void updateState(List<String> members, String state) {
		memMapper.updateState(members, state);
	}
	
	@Override
	public void updateReportCnt(String username, int diff) {
		if(diff > 1) diff = 1;
		else if(diff < -1) diff = -1;
		else if(diff == 0) return;
		memMapper.updateReportCnt(username, diff);
	}

	@Override
	public List<MemberVO> getMemberByKeyword(String keyword) {
		return memMapper.readUserByKeyword(keyword);
	}

}
