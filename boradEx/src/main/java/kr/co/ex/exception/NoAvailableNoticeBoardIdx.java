package kr.co.ex.exception;

public class NoAvailableNoticeBoardIdx extends RuntimeException {
	
	private int start;
	private int end;
	
	public NoAvailableNoticeBoardIdx(int start, int end){
		this.start = start;
		this.end = end;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + "\n["+ start + ", " + end + ") �߿� ��� ������ �Խñ� ��ȣ�� �����ϴ�.";
	}

}
