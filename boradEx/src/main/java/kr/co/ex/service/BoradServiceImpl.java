package kr.co.ex.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.exception.BadLikeUpdateException;
import kr.co.ex.mapper.BoardMapper;
import kr.co.ex.mapper.ReplyMapper;
import kr.co.ex.mapper.UserLikeMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoradServiceImpl implements BoardService {

	@NonNull
	private BoardMapper mapper;
	@NonNull
	private ReplyMapper replyMapper;
	@NonNull
	private UserLikeMapper likeMapper;
	
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
		BoardVO board = mapper.read(bno);
		board.setReplyCnt(likeMapper.readLikeCnt(bno));
		return board;
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
	@Transactional
	public void updateLike(int bno, int diff, String username) throws Exception {	
		if(diff > 0){
			likeMapper.addLike(bno, username);
			mapper.updateLike(bno, 1);
		}
		else if(diff < 0){
			likeMapper.subLike(bno, username);
			mapper.updateLike(bno, -1);
		}
		else throw new BadLikeUpdateException();
		
		
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

	// ÇØ±«¸ÁÃø
	@Override
	public List<BoardVO> listCriteria(Criteria cri) throws Exception {
		return mapper.listCriteria(cri).stream()
				.map(l -> {
					try {
						l.setReplyCnt(replyMapper.totalCount(l.getBno(), false));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return l;
				})
				.collect(Collectors.toList());
	}

	@Override
	public List<BoardVO> listRegdate(LocalDateTime regdate) throws Exception {
		return mapper.listRegdate(regdate);
	}

	@Override
	public int getTotalCount() throws Exception {
		return mapper.totalCount();
	}

	@Override
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception {
		return mapper.listSearch(cri).stream()
				.map(l -> {
					try {
						l.setReplyCnt(replyMapper.totalCount(l.getBno(), false));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return l;
				})
				.collect(Collectors.toList());
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

	@Override
	public int getTotalCntByWriter(String username) throws Exception {
		return mapper.readTotalCntByWriter(username);
	}

	@Override
	public List<BoardVO> listBoardByWriter(String writer) throws Exception {
		return mapper.listBoardByWriter(writer);
	}
	
}
