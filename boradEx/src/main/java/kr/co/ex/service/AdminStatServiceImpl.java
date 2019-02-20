package kr.co.ex.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.ex.exception.UndefinedStatDateTypeException;
import kr.co.ex.mapper.AdminStatMapper;
import kr.co.ex.util.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@RequiredArgsConstructor
public class AdminStatServiceImpl implements AdminStatService {
	
	@NonNull
	private AdminStatMapper mapper;
	
	public List<Long> getUserJoinCount(char type){
		return mapper.getUserJoinCount(getCriteriaDate(type));

	}
	
	public List<Long> getUserLeaveCount(char type){
		return mapper.getUserJoinCount(DateUtils.getADaysAgo(7));
	}
	
	public List<Long> getPostCount(char type){
		return mapper.getUserJoinCount(DateUtils.getADaysAgo(7));
	}
	
	private LocalDateTime getCriteriaDate(char type){
		switch(type){
		case 'd':
			return DateUtils.getADaysAgo(7);
			
		case 'w':
			return DateUtils.getAFewWeeksAgo(7);
			
		case 'm':
			return DateUtils.getAFewMonthAgo(7);
			
		case 'y':
			return DateUtils.getAFewYearsAgo(7);
			
		default:
			throw new UndefinedStatDateTypeException();
		}
	}
}
