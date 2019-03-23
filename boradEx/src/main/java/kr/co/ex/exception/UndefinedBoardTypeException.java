package kr.co.ex.exception;

public class UndefinedBoardTypeException extends RuntimeException {
	private String type;
	
	public UndefinedBoardTypeException(String type) {
		this.type = type;
	}

	@Override
	public String getMessage() {
		return this.type+" ��(��) ���ǵ��� ���� �Խ��� Ÿ���Դϴ�.";
	}

}
