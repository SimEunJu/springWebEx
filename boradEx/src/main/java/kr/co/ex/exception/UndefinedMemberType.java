package kr.co.ex.exception;

import javax.management.RuntimeErrorException;

public class UndefinedMemberType extends RuntimeException {

	@Override
	public String getMessage() {
		return "���ǵ��� ���� ����� Ÿ���Դϴ�.";
	}

}
