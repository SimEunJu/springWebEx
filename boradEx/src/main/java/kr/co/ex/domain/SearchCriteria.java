package kr.co.ex.domain;

import lombok.Data;

@Data
public class SearchCriteria extends Criteria {
	private String searchType;
	private String keyword;
	
	public String[] getTypeArr(){
		return searchType == null ? null : searchType.split("");
	}
}
