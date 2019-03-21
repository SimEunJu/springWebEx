package kr.co.ex.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.SearchCriteria;

@Service
public interface BoardService {
	
	public void register(BoardVO board) throws Exception;
	
	public BoardVO read(int bno) throws Exception;
	public String getWriterName(int bno);
	
	public void modify(BoardVO board) throws Exception;
	public void updateLike(int bno, int diff, String username) throws Exception;
	
	//public void remove(int bno) throws Exception;
	
	public List<BoardVO> listAll() throws Exception;
	public List<BoardVO> listCriteria(Criteria cri) throws Exception;
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception;
	public List<BoardVO> listByRegdate(LocalDateTime regdate) throws Exception;
	public List<BoardVO> listByWriter(String writer, Criteria cri) throws Exception;
	
	public int getSearchCnt(SearchCriteria cri) throws Exception;
	public int getTotalCnt(Criteria cri) throws Exception;
	public int getTotalCntByWriter(String username) throws Exception;
	
	public List<AttachVO> getAttach(int bno) throws Exception;
	public void removeAttach(String fullName) throws Exception;
	
	public int getReplyCnt(int bno) throws Exception;
	
	public void updateViewCnt(int bno) throws Exception;
	public void updateReportCnt(int bno, int diff) throws Exception;
}
