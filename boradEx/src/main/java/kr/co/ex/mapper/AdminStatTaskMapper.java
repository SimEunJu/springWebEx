package kr.co.ex.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ex.domain.UserStatVO;

@Mapper
public interface AdminStatTaskMapper {
	public int getJoinUserCount(LocalDateTime startDate, LocalDateTime endDate);
	public int getLeaveUserCount(LocalDateTime startDate, LocalDateTime endDate);
	public int getPostCount(LocalDateTime startDate, LocalDateTime endDate);
	public int getVisitCount(long startEpochSecond, long endEpochSecond);
	public void createUserStat(UserStatVO vo);
}
