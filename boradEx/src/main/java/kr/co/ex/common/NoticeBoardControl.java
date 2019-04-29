package kr.co.ex.common;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.exception.CorruptReleseOnNoticeIdx;
import kr.co.ex.exception.NoAvailableNoticeBoardIdx;
import kr.co.ex.util.BoardType;
import lombok.extern.log4j.Log4j2;

// 공지글 생성할 때 사용
// 삽입 가능한 게시글 번호 목록 관리
@Component
@Log4j2
public class NoticeBoardControl {
	
	private final int START_IDX;
	private final int END_IDX;
	
	private List<Boolean> idxUseList;
	private NoticeBoardCriteria notiCri;
	
	public NoticeBoardControl(int start, int end){
		notiCri = new NoticeBoardCriteria();
		notiCri.setPerPageNum(notiCri.getNoticeEndIdx() - notiCri.getNoticeStartIdx());
		notiCri.setPage(1);
		
		START_IDX = start;
		END_IDX	= end;
		
		// [start, end)
		idxUseList = new CopyOnWriteArrayList<>();
		for(int i=START_IDX; i<=END_IDX; i++){
			idxUseList.add(false);
		}
	}
	
	public NoticeBoardCriteria getNoticeBoardCri(SearchCriteria searchCri){
		NoticeBoardCriteria cri = new NoticeBoardCriteria();
		cri.setPage(searchCri.getPerPageNum());
		cri.setPage(searchCri.getPage());
		cri.setKeyword(searchCri.getKeyword());
		cri.setSearchType(searchCri.getSearchType());
		return cri;
	}
	
	public NoticeBoardCriteria getNoticeBoardCriCoverAll(){
		return notiCri;
	}
	
	public class NoticeBoardCriteria extends SearchCriteria {

		public NoticeBoardCriteria(){
			this.type = BoardType.NOTICE;
		}
		
		public int getNoticeStartIdx(){
			return START_IDX;
		}
		
		public int getNoticeEndIdx(){
			return END_IDX;
		}
		
		public String toString(){
			return "[SearchCriteria] : noticeEndIdx = "+START_IDX+", noticeEndIdx = "+END_IDX;
		}
	}
	
	private int getMinIdx(){
		int len = idxUseList.size();
		log.info(idxUseList.toString());
		for(int i=0; i<len; i++){
			if(idxUseList.get(i) == false) return i; 
		}
		throw new NoAvailableNoticeBoardIdx(START_IDX, END_IDX);
	}
	
	private boolean setUseIdx(int idx){
		idxUseList.set(idx, true);
		return true;
	}
	private boolean unsetUseIdx(int idx){
		idxUseList.set(idx, false);
		return true;
	}
	
	public synchronized int requestNotiBoardIdx(){
		int idx = getMinIdx();
		setUseIdx(idx);
		return idx + START_IDX;
	}
	
	public synchronized boolean rollbackRequestNotiBoardIdx(int idx){
		unsetUseIdx(idx);
		return true;
	}
	
	public synchronized boolean rollbackReleaseNotiBoardIdx(int idx){
		setUseIdx(idx);
		return true;
	}
	
	public synchronized boolean releaseNotiBoardIdx(int idx){
		if(idxUseList.get(idx) == false) throw new CorruptReleseOnNoticeIdx(idx);
		unsetUseIdx(idx);
		return true;
	}
	
}
