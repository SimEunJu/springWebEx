package kr.co.ex.service;

import java.util.List;

public interface AdminStatService {
	public List<Long> getUserJoinCount(char type);
	public List<Long> getUserLeaveCount(char type);
	public List<Long> getPostCount(char type);
}
