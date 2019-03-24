package kr.co.ex.exception;

public class CorruptReleseOnNoticeIdx extends RuntimeException {
	
	private int idx;
	
	public CorruptReleseOnNoticeIdx(int idx){
		this.idx = idx;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + "\n사용 중이지 않은 공지사항 게시글 번호("+ idx+ ")를 해지하려 했습니다.";
	}

}
