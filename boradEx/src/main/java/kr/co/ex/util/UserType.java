package kr.co.ex.util;

public enum UserType {
	ALL("all", "모두"), 
	ACTIVE("A", "정상"), 
	REPORT("R", "일정 신고 수 이상 받음"), 
	BANNED("B", "정지"), 
	SLEEP("S", "휴면"), 
	LEAVE("L", "탈퇴");
	
	final private String type;
	final private String korDescription;
	
	private UserType(String type, String korDescription){
		this.type = type;
		this.korDescription = korDescription;
	}
	
	public String getTypeInitial(){
		return type;
	}
	
	public String getKorDescription(){
		return korDescription;
	}
	
	//abstract public String getTypeInitial();
}
