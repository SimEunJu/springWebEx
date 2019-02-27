package kr.co.ex.exception;

import javax.management.RuntimeErrorException;

public class UndefinedMemberType extends RuntimeException {

	@Override
	public String getMessage() {
		return "정의되지 않은 사용자 타입입니다.";
	}

}
