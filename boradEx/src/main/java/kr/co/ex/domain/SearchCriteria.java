package kr.co.ex.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString 
@AllArgsConstructor @NoArgsConstructor 
public class SearchCriteria extends Criteria {
	
	private String searchType;
	private String keyword;
	
	public void setKeyword(String keyword){
		if(keyword != null && keyword.trim().length() == 0) this.keyword = keyword;
		else this.keyword = keyword;
	}
	
	private String encoding(String keyword){
		if(keyword == null) return "";
		try{
			return URLEncoder.encode(keyword, "UTF-8");
		}catch(UnsupportedEncodingException e){
			return "";
		}
	}
	
	public String makeSearch(){
		UriComponents uri = UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("perPageNum", perPageNum)
				.queryParam("searchType", searchType)
				.queryParam("keyword", encoding(keyword))
				.build();
		return uri.toUriString();
	}
	
	public String makeSearch(int page){
		UriComponents uri = UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("perPageNum", perPageNum)
				.queryParam("searchType", searchType)
				.queryParam("keyword", encoding(keyword))
				.build();
		return uri.toUriString();
	}
	
	public String[] getTypeArr(){
		return searchType == null ? null : searchType.split("");
	}
}
