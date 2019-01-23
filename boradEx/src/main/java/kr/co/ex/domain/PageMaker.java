package kr.co.ex.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
	public String makeQuery(int page){
		UriComponents uri = UriComponentsBuilder.newInstance()
				// page is variable
				.queryParam("page", page)
				// num of per page article is fixed
				.queryParam("perPageNum", cri.getPerPageNum())
				.build();
		return uri.toUriString();
	}
	public String makeSearch(int page){
		UriComponents uri = UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("perPageNum", cri.getPerPageNum())
				.queryParam("searchType", ((SearchCriteria)cri).getSearchType())
				.queryParam("keyword", encoding(((SearchCriteria)cri).getKeyword()))
				.build();
		return uri.toUriString();
	}
	private String encoding(String keyword){
		if(keyword==null || keyword.trim().length()==0) return "";
		try{
			keyword = keyword.trim();
			return URLEncoder.encode(keyword, "UTF-8");
		}catch(UnsupportedEncodingException e){
			return "";
		}
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
