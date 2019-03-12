package kr.co.ex.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;

import kr.co.ex.domain.UserStatVO;

@Mapper
public interface AdminStatTaskMapper {
	public int getJoinUserCount(LocalDateTime date);
	public int getLeaveUserCount(LocalDateTime date);
	public int getPostCount(LocalDateTime date);
	public int getVisitCount(long epochSecond);
	public void createUserStat(UserStatVO vo);
}
