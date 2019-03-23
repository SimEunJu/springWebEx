package kr.co.ex.exception;

public class UndefinedBoardTypeException extends RuntimeException {
	private String type;
	
	public UndefinedBoardTypeException(String type) {
		this.type = type;
	}

	@Override
	public String getMessage() {
		return this.type+" 은(는) 정의되지 않은 게시판 타입입니다.";
	}

}
