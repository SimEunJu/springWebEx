package kr.co.ex.persistence;

import java.util.List;

import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.SearchCriteria;

public interface BoardDAO {
	public void create(BoardVO vo) throws Exception;
	public BoardVO read(Integer bno) throws Exception;
	public void update(BoardVO vo) throws Exception;
	public void delete(Integer bno) throws Exception;
	public List<BoardVO> listAll() throws Exception;
	public List<BoardVO> listCriteria(Criteria cri) throws Exception;
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception;
	public int searchCount(SearchCriteria cri) throws Exception;
	public int totalCount() throws Exception;
	public void addAttach(String fullName) throws Exception;
	public List<String> getAttach(Integer bno) throws Exception;
	public void deleteAttach(String fullName) throws Exception;
	public void deleteAllAttach(Integer bno) throws Exception;
	public void replaceAttach(String fullName, Integer bno) throws Exception;
	
	public int readReplyCnt(int bno) throws Exception;
	public void updateViewCnt(int bno) throws Exception;
}
