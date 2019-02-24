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
	
	public BoardVO read(Integer bno) throws Exception;
	
	public void modify(BoardVO board) throws Exception;
	public void updateLike(int bno, int diff, String username) throws Exception;
	
	public void remove(Integer bno) throws Exception;
	
	public List<BoardVO> listAll() throws Exception;
	public List<BoardVO> listCriteria(Criteria cri) throws Exception;
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception;
	public List<BoardVO> listRegdate(LocalDateTime regdate) throws Exception;
	
	public int getSearchCount(SearchCriteria cri) throws Exception;
	public int getTotalCount() throws Exception;
	
	public List<AttachVO> getAttach(Integer bno) throws Exception;
	public void removeAttach(String fullName) throws Exception;
	
	public int getReplyCnt(int bno) throws Exception;
	
	public void updateViewCnt(int bno) throws Exception;
}
