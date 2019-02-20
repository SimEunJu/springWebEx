package kr.co.ex.service;

public interface UserLikeService {
	public void addLike(int bno, String username) throws Exception;
	public void subList(int bno, String username) throws Exception;
	public boolean isUserLiked() throws Exception;
}	
