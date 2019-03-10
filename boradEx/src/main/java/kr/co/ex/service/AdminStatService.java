package kr.co.ex.service;

import java.util.List;

public interface AdminStatService {
	public List<Long> getUserJoinCount(String type);
	public List<Long> getUserLeaveCount(String type);
	public List<Long> getPostCount(String type);
}
