package kr.co.ex.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLikeMapper {
	
	public void addLike(@Param("bno") int bno, @Param("username") String username);
	public void subLike(@Param("bno") int bno, @Param("username") String username);
	
	public boolean isUserLiked(@Param("username") String username, @Param("bno") int bno);
	public int readLikeCnt(int bno);
}
