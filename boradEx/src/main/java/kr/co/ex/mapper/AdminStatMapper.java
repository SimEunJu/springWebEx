package kr.co.ex.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminStatMapper {
	
	public Integer getUserJoinCount(@Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);
	public Integer getUserLeaveCount(@Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);
	public Integer getPostCount(@Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);
	public Integer getVisitCount(@Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);
}
