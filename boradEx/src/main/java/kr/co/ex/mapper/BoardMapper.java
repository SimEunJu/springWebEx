package kr.co.ex.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.SearchCriteria;

@Mapper
public interface BoardMapper {
	
	public void create(BoardVO vo) throws Exception;
	public BoardVO read(Integer bno) throws Exception;
	
	public void update(BoardVO vo) throws Exception;
	public void updateLike(@Param("bno") int bno, @Param("diff") int diff) throws Exception;
	
	public void delete(Integer bno) throws Exception;
	
	public List<BoardVO> listAll() throws Exception;
	public List<BoardVO> listCriteria(Criteria cri) throws Exception;
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception;
	public List<BoardVO> listRegdate(LocalDateTime regdate) throws Exception;
	public List<BoardVO> listBoardByWriter(String writer) throws Exception;
	
	public int searchCount(SearchCriteria cri) throws Exception;
	public int totalCount() throws Exception;
	public int readTotalCntByWriter(String username) throws Exception;
	
	public String readWriterName(int bno);
	
	public void addAttach(AttachVO file) throws Exception;
	public List<AttachVO> getAttach(Integer bno) throws Exception;
	public void deleteAttach(String uuid) throws Exception;
	public void deleteAllAttach(Integer bno) throws Exception;
	public void replaceAttach(AttachVO file) throws Exception;
	
	public int readReplyCnt(int bno) throws Exception;
	public void updateViewCnt(int bno) throws Exception;

}