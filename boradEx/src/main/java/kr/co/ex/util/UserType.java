package kr.co.ex.util;

public enum UserType {
	ALL("all"), 
	ACTIVE("A"), 
	REPORT("R"), 
	BANNED("B"), 
	SLEEP("S"), 
	LEAVE("L");
	
	final private String type;
	
	private UserType(String type){
		this.type = type;
	}
	
	public String getTypeInitial(){
		return type;
	}
	
	//abstract public String getTypeInitial();
}
