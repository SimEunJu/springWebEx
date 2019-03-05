package kr.co.ex.service;

public interface UserLikeService {
	public void addLike(int bno, String username) throws Exception;
	public void subList(int bno, String username) throws Exception;
	
	public boolean isUserLiked(int bno) throws Exception;
	public int getLikeCnt(int bno) throws Exception;
}	
