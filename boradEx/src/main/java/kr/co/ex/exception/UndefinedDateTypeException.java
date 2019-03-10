package kr.co.ex.exception;

public class UndefinedDateTypeException extends RuntimeException {
	private String dateType;

	public UndefinedDateTypeException(String dateType) {
		this.dateType = dateType;
	}

	@Override
	public String getMessage() {
		return this.dateType + "��(��) ���ǵ��� ���� Ÿ���Դϴ�.";
	}
	
}
