package kr.co.ex.common;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import kr.co.ex.domain.Criteria;
import kr.co.ex.util.BoardType;
import lombok.AllArgsConstructor;

// 공지글 생성할 때 사용
// 삽입 가능한 idx 목록 관리
@Component
@AllArgsConstructor
public class NoticBoardControl {
	
	private List<Boolean> idxUseList;
	private NoticeBoardCriteria notiCri;
	
	public NoticBoardControl(){
		notiCri = new NoticeBoardCriteria();
		notiCri.setPerPageNum(notiCri.getNoticeEndIdx() - notiCri.getNoticeStartIdx());
		notiCri.setPage(1);
	}
	
	public NoticeBoardCriteria getNoticeBoardCri(int page, int perPageNum){
		NoticeBoardCriteria cri = new NoticeBoardCriteria();
		cri.setPage(perPageNum);
		cri.setPage(page);
		return cri;
	}
	
	public NoticeBoardCriteria getNoticeBoardCri(){
		return notiCri;
	}
	
	private class NoticeBoardCriteria extends Criteria {
		private final static int NOTICE_START_IDX = 20;
		private final static int NOTICE_END_IDX = 50;
		
		public NoticeBoardCriteria(){
			this.type = BoardType.NOTICE;
		}
		
		public int getNoticeStartIdx(){
			return NOTICE_START_IDX;
		}
		
		public int getNoticeEndIdx(){
			return NOTICE_END_IDX;
		}
		
		public String toString(){
			return "[SearchCriteria] : noticeEndIdx = "+NOTICE_START_IDX+", noticeEndIdx = "+NOTICE_END_IDX;
		}
	}
	
	private int getMinIdx(){
		int range = notiCri.getNoticeEndIdx() - notiCri.getNoticeStartIdx();
		OptionalInt minIdx = IntStream.range(0, range).filter(i -> idxUseList.get(i) == false).findFirst();
		if(minIdx.isPresent()) return minIdx.getAsInt();
		return new NoAvailableNoticeBoardIdx();
	}
	
	private boolean useIdx(int idx){
		idxUseList.set(idx, true);
		return true;
	}
	
	public synchronized int requestNotiBoardIdx(){
		int idx = getMinIdx();
		useIdx(idx);
		return idx;
	}
	
}
