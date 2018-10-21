package kr.co.ex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.persistence.BoardDAO;

@Service
public class BoradServiceImpl implements BoardService {

	@Autowired
	private BoardDAO dao;
	
	@Override
	@Transactional
	public void register(BoardVO board) throws Exception {
		dao.create(board);
		String[] files = board.getFiles();
		if(files != null){
			for(String file : files){
				dao.addAttach(file);
			}
		}
	}
	
	@Override
	public BoardVO read(Integer bno) throws Exception {
		return dao.read(bno);
	}

	@Override
	@Transactional
	public void modify(BoardVO board) throws Exception {
		dao.update(board);
		Integer bno = board.getBno();
		dao.deleteAllAttach(bno);
		String[] files = board.getFiles();
		if(files != null){
			for(String file: files){
				dao.replaceAttach(file, bno);
			}
		}
		
	}

	@Override
	@Transactional
	public void remove(Integer bno) throws Exception {
		dao.deleteAllAttach(bno);
		dao.delete(bno);
	}

	@Override
	public List<BoardVO> listAll() throws Exception {
		return dao.listAll();
	}

	@Override
	public List<BoardVO> listCriteria(Criteria cri) throws Exception {
		return dao.listCriteria(cri);
	}

	@Override
	public int getTotalCount() throws Exception {
		return dao.totalCount();
	}

	@Override
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception {
		return dao.listSearch(cri);
	}

	@Override
	public int searchCount(SearchCriteria cri) throws Exception {
		return dao.searchCount(cri);
	}

	@Override
	public List<String> getAttach(Integer bno) throws Exception {
		return dao.getAttach(bno);
	}

	@Override
	public void removeAttach(String fullName) throws Exception {
		dao.deleteAttach(fullName);
	}

	@Override
	public int getReplyCnt(int bno) throws Exception {
		return dao.readReplyCnt(bno);
	}

	@Override
	public void updateViewCnt(int bno) throws Exception {
		dao.updateViewCnt(bno);
	}
	
}
