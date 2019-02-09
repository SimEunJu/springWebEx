package kr.co.ex.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

@Data
public class SearchCriteria extends Criteria {
	private String searchType;
	private String keyword;
	
	private String encoding(String keyword){
		//keyword = keyword.trim();
		if(keyword==null || keyword.length()==0) return "";
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
