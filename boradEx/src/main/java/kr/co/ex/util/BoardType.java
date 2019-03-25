package kr.co.ex.util;

public enum BoardType {
	ALL(0, "all"),
	HOT(2, "hot"),
	NOTICE(1, "notice");
	
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