package kr.co.ex.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminStatMapper {
	
	public Long getUserJoinCount(@Param("startDay") String startDay, @Param("endDay") String endDay);
	public Long getUserLeaveCount(@Param("startDay") String startDay, @Param("endDay") String endDay);
	public Long getPostCount(@Param("startDay") String startDay, @Param("endDay") String endDay);

}
