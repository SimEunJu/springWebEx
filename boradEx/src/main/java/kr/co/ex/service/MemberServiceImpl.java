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
			return listMember(cri.getPage(), cri.getPerPageNum());
		case ACTIVE:
		case BANNED:
		case SLEEP:
		case LEAVE:
			return listStateMember(cri.getPage(), cri.getPerPageNum(), type.getTypeInitial());
		case REPORT:
			return listReportMember(cri.getPage(), cri.getPerPageNum());
		default:
			throw new UndefinedMemberType();
		}
	}
	
	@Override
	public List<MemberVO> listMember(int start, int end) {
		return memMapper.listMember(start, end);
	}

	@Override
	public List<MemberVO> listReportMember(int start, int end) {
		return memMapper.listReportMember(start, end);
	}

	@Override
	public List<MemberVO> listStateMember(int start, int end, String state) {
		return memMapper.listStateMember(start, end, state);
	}

	@Override
	public int getMemberCnt() {
		return memMapper.readMemberCnt();
	}

	@Override
	public void updateState(List<String> members, String state) {
		memMapper.updateState(members, state);
	}

	
}
