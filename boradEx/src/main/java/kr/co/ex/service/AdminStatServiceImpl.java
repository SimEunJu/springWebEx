package kr.co.ex.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private AdminStatMapper statMapper;
	
	@Transactional
	public List<Long> getUserJoinCount(char type){
		List<String> cri = getCriteriaDate(type);
		List<Long> data = new ArrayList<>();
		int len = cri.size();
		for(int i=0; i<len; i++){
			if(i == len-1){
				data.add(statMapper.getUserJoinCount(cri.get(i), "now()"));
			}
			else data.add(statMapper.getUserJoinCount(cri.get(i), cri.get(i+1)));
		}
		return data;
	}
	
	@Transactional
	public List<Long> getUserLeaveCount(char type){
		List<String> cri = getCriteriaDate(type);
		List<Long> data = new ArrayList<>();
		int len = cri.size();
		for(int i=0; i<len; i++){
			if(i == len-1){
				data.add(statMapper.getUserLeaveCount(cri.get(i), "now()"));
			}
			else data.add(statMapper.getUserLeaveCount(cri.get(i), cri.get(i+1)));
		}
		return data;
	}
	
	@Transactional
	public List<Long> getPostCount(char type){
		List<String> cri = getCriteriaDate(type);
		List<Long> data = new ArrayList<>();
		int len = cri.size();
		for(int i=0; i<len; i++){
			if(i == len-1){
				data.add(statMapper.getPostCount(cri.get(i), "now()"));
			}
			else data.add(statMapper.getPostCount(cri.get(i), cri.get(i+1)));
		}
		return data;
	}

	private List<String> getCriteriaDate(char type){
		List<String> cris = new ArrayList<>();
		List<LocalDateTime> timeCris = new ArrayList<>();
		switch(type){
		case 'd':
			LocalDateTime yesterday = DateUtils.getADaysAgo(0);
			for(int i=5; i>=0; i--){
				timeCris.add(yesterday.minusDays(i));
			}
			cris = timeCris.stream().map(c -> c.format(DateTimeFormatter.ISO_LOCAL_DATE)).collect(Collectors.toList());
			break;
		case 'w':
			LocalDateTime lastWeek = DateUtils.getAFewWeeksAgo(0);
			for(int i=5; i>=0; i--){
				timeCris.add(lastWeek.minusDays(i));
			}
			cris = timeCris.stream().map(c -> c.format(DateTimeFormatter.ISO_LOCAL_DATE)).collect(Collectors.toList());
			break;
		case 'm':
			LocalDateTime lastMonth = DateUtils.getAFewMonthAgo(0);
			for(int i=5; i>=0; i--){
				timeCris.add(lastMonth.minusDays(i));
			}
			cris = timeCris.stream().map(c -> c.format(DateTimeFormatter.ISO_LOCAL_DATE)).collect(Collectors.toList());
			break;
		default:
			throw new UndefinedStatDateTypeException();
		}
		return cris;
	}
}
