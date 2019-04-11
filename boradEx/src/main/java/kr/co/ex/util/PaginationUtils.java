package kr.co.ex.util;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.PageMaker;

public class PaginationUtils {
	public static PageMaker pagination(Criteria cri, int totalCnt){
		PageMaker pm = new PageMaker();
		
		if("prve".equals(cri.getMove())){
			int page = cri.getPage() - cri.getPerPageNum();
			cri.setPage(page);
		}
		else if("next".equals(cri.getMove())){
			int page = cri.getPage() + 1;
			cri.setPage(page);
		}
		
		pm.setCri(cri);
		pm.setTotalCount(totalCnt);
		
		return pm;
	}
}
