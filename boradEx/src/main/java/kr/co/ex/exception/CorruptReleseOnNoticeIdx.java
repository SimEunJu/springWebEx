package kr.co.ex.exception;

public class CorruptReleseOnNoticeIdx extends RuntimeException {
	
	private int idx;
	
	public CorruptReleseOnNoticeIdx(int idx){
		this.idx = idx;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + "\n��� ������ ���� �������� �Խñ� ��ȣ("+ idx+ ")�� �����Ϸ� �߽��ϴ�.";
	}

}
