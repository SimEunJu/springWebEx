package kr.co.ex.domain;

public class PageMaker {
	private int totalCount;
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;
	
	private Criteria cri;
	private int displayPageNum = 10;
	
	// 1. set criteria
	public void setCri(Criteria cri){
		this.displayPageNum = cri.getPerPageNum();
		this.cri = cri;
	}
	// 2. set total count
	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
		calcData();
	}
	// 3. calculate other variables
	private void calcData(){
		endPage = (int)(Math.ceil(cri.getPage()/(double)displayPageNum)*displayPageNum);
		startPage = endPage - displayPageNum + 1;
		int tempEndPage = (int)Math.ceil(totalCount/(double)cri.getPerPageNum());
		if(tempEndPage < endPage) endPage = tempEndPage;
		
		prev = startPage == 1 ? false : true;
		next = endPage*cri.getPerPageNum() >= totalCount ? false : true;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public boolean isPrev() {
		return prev;
	}
	public void setPrev(boolean prev) {
		this.prev = prev;
	}
	public boolean isNext() {
		return next;
	}
	public void setNext(boolean next) {
		this.next = next;
	}
	public int getDisplayPageNum() {
		return displayPageNum;
	}
	public void setDisplayPageNum(int displayPageNum) {
		this.displayPageNum = displayPageNum;
	}
	public Criteria getCri() {
		return cri;
	}
	@Override
	public String toString() {
		return "PageMaker [totalCount=" + totalCount + ", startPage=" + startPage + ", endPage=" + endPage + ", prev="
				+ prev + ", next=" + next + ", cri=" + cri + ", displayPageNum=" + displayPageNum + "]";
	}
	
}
