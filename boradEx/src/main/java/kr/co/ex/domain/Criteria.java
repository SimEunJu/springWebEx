package kr.co.ex.domain;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import kr.co.ex.util.BoardType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter @Setter @ToString
public class Criteria {
	protected int page;
	// number of article in a page;
	protected int perPageNum;
	// prev or next
	protected String move;
	
	protected BoardType type;
	
	public Criteria() {
		this.page = 1;
		this.perPageNum = 10;
		this.type = BoardType.ALL;
	}
	
	public void setType(String mode){
		BoardType type = null;
		switch (mode) {
		case "all":
			type = BoardType.ALL;
			break;
		case "hot":
			type = BoardType.HOT;
			break;
		case "notice":
			type = BoardType.NOTICE;
			break;	
		case "report":
			type = BoardType.REPORT;
			break;
		case "self":
			type = BoardType.SELF;
			break;
		default:
			type = BoardType.ALL;
			break;
		}
		this.type = type;
	}
	
	public void setPage(int page) {
		if(page <= 0) this.page = 1;
		else this.page = page;
	}
	public void setPerPageNum(int perPageNum) {
		if(perPageNum <= 0 || perPageNum >= 100) this.perPageNum = 10;
		else this.perPageNum = perPageNum;
	}
	public int getPageStart(){
		return (this.page - 1)*this.perPageNum;
	}
	
	public String makeQuery(){
		UriComponents uri = UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("perPageNum", perPageNum)
				.build();
		return uri.toString();
	}
}
