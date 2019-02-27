package kr.co.ex.util;

public enum UserType {
	ALL("all"){
		public String getTypeInitial(){
			return "ALL";
		}
	}, 
	ACTIVE("active"){
		public String getTypeInitial(){
			return "A";
		}
	}, 
	REPORT("report"){
		public String getTypeInitial(){
			return "R";
		}
	}, 
	BANNED("banned"){
		public String getTypeInitial(){
			return "B";
		}
	}, 
	SLEEP("sleep"){
		public String getTypeInitial(){
			return "S";
		}
	}, 
	LEAVE("leave"){
		public String getTypeInitial(){
			return "L";
		}
	};
	
	final private String type;
	
	private UserType(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
	
	abstract public String getTypeInitial();
}
