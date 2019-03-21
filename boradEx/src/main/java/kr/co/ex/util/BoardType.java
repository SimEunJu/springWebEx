package kr.co.ex.util;

public enum BoardType {
	ALL("all"),
	HOT("hot"),
	NOTICE("notice");
	
	final private String type;
	
	private BoardType(String type){
		this.type = type;
	}
	
	public String getTypeInitial(){
		return type;
	}
}