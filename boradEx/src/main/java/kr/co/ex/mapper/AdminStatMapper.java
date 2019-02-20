package kr.co.ex.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminStatMapper {
	
	public List<Long> getUserJoinCount(LocalDateTime date);
	public List<Long> getUserLeaveCount(LocalDateTime date);
	public List<Long> getPostCount(LocalDateTime date);

}
