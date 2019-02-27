package kr.co.ex.util;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.PageMaker;

public class PaginationUtils {
	public static PageMaker pagination(String move, Criteria cri, int totalCnt){
		PageMaker pm = new PageMaker();
		
		int page = 1;
		if("prve".equals(move)){
			page = cri.getPage() - cri.getPerPageNum();
		}
		else if("next".equals(move)){
			page = cri.getPage() + 1;
		}
		cri.setPage(page);
		pm.setCri(cri);
		pm.setTotalCount(totalCnt);
		
		return pm;
	}
}
