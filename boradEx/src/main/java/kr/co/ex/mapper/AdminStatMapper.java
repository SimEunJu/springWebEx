package kr.co.ex.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminStatMapper {
	
	public int getUserJoinCount(@Param("startDay") LocalDateTime localDateTime, @Param("endDay") LocalDateTime localDateTime2);
	public int getUserLeaveCount(@Param("startDay") LocalDateTime localDateTime, @Param("endDay") LocalDateTime endDay);
	public int getPostCount(@Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);
	public int getVisitCount(@Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);
}
