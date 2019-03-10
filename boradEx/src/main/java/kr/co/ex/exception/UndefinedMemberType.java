package kr.co.ex.exception;

public class UndefinedMemberType extends RuntimeException {
	private String type;
	
	public UndefinedMemberType(String type) {
		this.type = type;
	}

	@Override
	public String getMessage() {
		return this.type+" ��(��) ���ǵ��� ���� ����� Ÿ���Դϴ�.";
	}

}
