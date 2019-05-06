package kr.co.ex.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.exception.UndefinedDateTypeException;
import kr.co.ex.mapper.AdminStatMapper;
import kr.co.ex.util.DateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class AdminStatServiceImpl implements AdminStatService {
	
	@NonNull private AdminStatMapper statMapper;
	
	@Transactional
	public List<Integer> getUserJoinCount(String type){
		List<LocalDateTime> cri = getCriteriaDate(type);
	
		List<Integer> data = new ArrayList<>();
		int len = cri.size();
		for(int i=0; i<len; i++){
			if(i == len-1){
				data.add(statMapper.getUserJoinCount(cri.get(i), LocalDateTime.now()));
			}
			else data.add(statMapper.getUserJoinCount(cri.get(i), cri.get(i+1)));
		}
		return data;
	}
	
	@Transactional
	public List<Integer> getUserLeaveCount(String type){
		List<LocalDateTime> cri = getCriteriaDate(type);
		List<Integer> data = new ArrayList<>();
		int len = cri.size();
		for(int i=0; i<len; i++){
			if(i == len-1){
				data.add(statMapper.getUserLeaveCount(cri.get(i), LocalDateTime.now()));
			}
			else data.add(statMapper.getUserLeaveCount(cri.get(i), cri.get(i+1)));
		}
		return data;
	}
	
	@Transactional
	public List<Integer> getPostCount(String type){
		List<LocalDateTime> cri = getCriteriaDate(type);
		List<Integer> data = new ArrayList<>();
		int len = cri.size();
		for(int i=0; i<len; i++){
			if(i == len-1){
				data.add(statMapper.getPostCount(cri.get(i), LocalDateTime.now()));
			}
			else data.add(statMapper.getPostCount(cri.get(i), cri.get(i+1)));
		}
		return data;
	}
	
	@Override
	public List<Integer> getVisitCount(String type) {
		List<LocalDateTime> cri = getCriteriaDate(type);
		List<Integer> data = new ArrayList<>();
		int len = cri.size();
		for(int i=0; i<len; i++){
			if(i == len-1){
				data.add(statMapper.getVisitCount(cri.get(i), LocalDateTime.now()));
			}
			else data.add(statMapper.getVisitCount(cri.get(i), cri.get(i+1)));
		}
		return data;
	}
	
	private List<LocalDateTime> getCriteriaDate(String type){
		List<LocalDateTime> timeCris = new ArrayList<>();
		switch(type){
		case "d":
			LocalDateTime today = DateUtils.getADaysAgo(0);
			for(int i=5; i>=0; i--){
				timeCris.add(today.minusDays(i));
			}
			break;
		case "w":
			LocalDateTime thisWeek = DateUtils.getAFewWeeksAgo(0);
			for(int i=5; i>=0; i--){
				timeCris.add(thisWeek.minusWeeks(i));
			}
			break;
		case "m":
			LocalDateTime thisMonth = DateUtils.getAFewMonthAgo(0);
			for(int i=5; i>=0; i--){
				timeCris.add(thisMonth.minusMonths(i));
			}
			break;
		default:
			throw new UndefinedDateTypeException(type);
		}
		return timeCris;
	}

	
}
