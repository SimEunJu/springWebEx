package kr.co.ex.service;

import java.util.List;

public interface AdminStatService {
	public List<Integer> getUserJoinCount(String type);
	public List<Integer> getUserLeaveCount(String type);
	public List<Integer> getPostCount(String type);
	public List<Integer> getVisitCount(String type);
}
