package kr.co.ex.exception;

public class UndefinedDateTypeException extends RuntimeException {
	private String dateType;

	public UndefinedDateTypeException(String dateType) {
		this.dateType = dateType;
	}

	@Override
	public String getMessage() {
		return this.dateType + "은(는) 정의되지 않은 타입입니다.";
	}
	
}
