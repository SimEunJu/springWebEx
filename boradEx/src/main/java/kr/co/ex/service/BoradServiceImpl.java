package kr.co.ex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.mapper.BoardMapper;

@Service
public class BoradServiceImpl implements BoardService {

	@Autowired
	private BoardMapper mapper;
	
	@Override
	@Transactional
	public void register(BoardVO board) throws Exception {
		mapper.create(board);
		List<AttachVO> files = board.getFiles();
		if(files != null){
			for(AttachVO file : files){
				mapper.addAttach(file);
			}
		}
	}
	
	@Override
	public BoardVO read(Integer bno) throws Exception {
		return mapper.read(bno);
	}

	@Override
	@Transactional
	public void modify(BoardVO board) throws Exception {
		mapper.update(board);
		Integer bno = board.getBno();
		mapper.deleteAllAttach(bno);
		List<AttachVO> files = board.getFiles();
		if(files != null){
			for(AttachVO file: files){
				mapper.replaceAttach(file);
			}
		}
		
	}

	@Override
	public void updateLike(int bno, int diff) throws Exception {
		mapper.updateLike(bno, diff);
	}

	@Override
	@Transactional
	public void remove(Integer bno) throws Exception {
		mapper.deleteAllAttach(bno);
		mapper.delete(bno);
	}

	@Override
	public List<BoardVO> listAll() throws Exception {
		return mapper.listAll();
	}

	@Override
	public List<BoardVO> listCriteria(Criteria cri) throws Exception {
		return mapper.listCriteria(cri);
	}

	@Override
	public int getTotalCount() throws Exception {
		return mapper.totalCount();
	}

	@Override
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception {
		return mapper.listSearch(cri);
	}

	@Override
	public int getSearchCount(SearchCriteria cri) throws Exception {
		return mapper.searchCount(cri);
	}

	@Override
	public List<AttachVO> getAttach(Integer bno) throws Exception {
		return mapper.getAttach(bno);
	}

	@Override
	public void removeAttach(String fullName) throws Exception {
		mapper.deleteAttach(fullName);
	}

	@Override
	public int getReplyCnt(int bno) throws Exception {
		return mapper.readReplyCnt(bno);
	}

	@Override
	public void updateViewCnt(int bno) throws Exception {
		mapper.updateViewCnt(bno);
	}
	
}
