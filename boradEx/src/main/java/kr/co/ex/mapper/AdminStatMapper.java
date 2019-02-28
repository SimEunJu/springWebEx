package kr.co.ex.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminStatMapper {
	
	public Long getUserJoinCount(@Param("startDay") LocalDateTime localDateTime, @Param("endDay") LocalDateTime localDateTime2);
	public Long getUserLeaveCount(@Param("startDay") LocalDateTime localDateTime, @Param("endDay") LocalDateTime endDay);
	public Long getPostCount(@Param("startDay") LocalDateTime startDay, @Param("endDay") LocalDateTime endDay);

}
