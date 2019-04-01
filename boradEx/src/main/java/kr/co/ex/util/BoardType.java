package kr.co.ex.util;

public enum BoardType {
	ALL(0, "all"),
	HOT(2, "hot"),
	NOTICE(1, "notice"),
	REPORT(3, "report"),
	SELF(4, "self");
	
	final private int type;
	final private String initial;
	
	private BoardType(int type, String initial){
		this.type = type;
		this.initial = initial;
	}
	public String getTypeInitial(){
		return initial;
	}
	public int getTypeNumber(){
		return type;
	}
}