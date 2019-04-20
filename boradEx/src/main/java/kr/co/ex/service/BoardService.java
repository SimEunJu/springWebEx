package kr.co.ex.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.ex.common.NoticeBoardControl;
import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.SearchCriteria;

@Service
public interface BoardService {
	
	public void register(BoardVO board) throws Exception;
	public void registerNotice(BoardVO vo) throws Exception;
	
	public BoardVO read(int bno) throws Exception;
	public boolean matchPassword(int bno, String password) throws Exception;
	public String getWriterName(int bno);
	
	public void modify(BoardVO board) throws Exception;
	public void updateLike(int bno, int diff, String username) throws Exception;
	
	public void remove(BoardVO vo) throws Exception;
	public void removeNoti(int bno) throws Exception;
	public void removeByWriter();
	
	public List<BoardVO> listAll() throws Exception;
	public List<BoardVO> listCriteria(Criteria cri) throws Exception;
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception;
	public List<BoardVO> listByRegdate(LocalDateTime regdate) throws Exception;
	public List<BoardVO> listByWriter(String writer, SearchCriteria cri) throws Exception;
	public List<BoardVO> listNotice(NoticeBoardControl.NoticeBoardCriteria cri) throws Exception;
	
	public int getNoticeCnt(NoticeBoardControl.NoticeBoardCriteria cri) throws Exception;
	public int getSearchCnt(SearchCriteria cri) throws Exception;
	public int getTotalCnt(Criteria cri) throws Exception;
	public int getTotalCntByWriter(String username, SearchCriteria cri) throws Exception;
	
	public List<AttachVO> getAttach(int bno) throws Exception;
	public void removeAttach(String fullName) throws Exception;
	
	public int getReplyCnt(int bno) throws Exception;
	
	public void updateViewCnt(int bno) throws Exception;
	public void updateReportCnt(int bno, int diff) throws Exception;
}
