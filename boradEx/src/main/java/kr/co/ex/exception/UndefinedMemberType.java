package kr.co.ex.exception;

public class UndefinedMemberType extends RuntimeException {

	@Override
	public String getMessage() {
		return "���ǵ��� ���� ����� Ÿ���Դϴ�.";
	}

}
