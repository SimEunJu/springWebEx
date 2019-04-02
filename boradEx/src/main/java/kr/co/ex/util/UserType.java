package kr.co.ex.util;

public enum UserType {
	ALL("all", "���"), 
	ACTIVE("A", "����"), 
	REPORT("R", "���� �Ű� �� �̻� ����"), 
	BANNED("B", "����"), 
	SLEEP("S", "�޸�"), 
	LEAVE("L", "Ż��");
	
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
