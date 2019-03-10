package kr.co.ex.exception;

public class UndefinedMemberType extends RuntimeException {
	private String type;
	
	public UndefinedMemberType(String type) {
		this.type = type;
	}

	@Override
	public String getMessage() {
		return this.type+" 은(는) 정의되지 않은 사용자 타입입니다.";
	}

}
