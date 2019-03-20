package kr.co.ex.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.annoation.Loggable;
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

	@NonNull private BoardMapper boardMapper;
	@NonNull private ReplyMapper replyMapper;
	@NonNull private UserLikeMapper likeMapper;
	
	@Override
	@Transactional
	public void register(BoardVO board) throws Exception {
		boardMapper.create(board);
		List<AttachVO> files = board.getFiles();
		if(files != null){
			for(AttachVO file : files){
				boardMapper.addAttach(file);
			}
		}
	}
	
	@Override
	@Transactional
	public BoardVO read(int bno) throws Exception {
		BoardVO board = boardMapper.read(bno);
		board.setReplyCnt(likeMapper.readLikeCnt(bno));
		return board;
	}

	@Override
	@Transactional
	public void modify(BoardVO board) throws Exception {
		boardMapper.update(board);
		int bno = board.getBno();
		boardMapper.deleteAllAttach(bno);
		List<AttachVO> files = board.getFiles();
		if(files != null){
			for(AttachVO file: files){
				boardMapper.replaceAttach(file);
			}
		}
		
	}

	@Override
	@Transactional
	public void updateLike(int bno, int diff, String username) throws Exception {	
		if(diff > 0){
			likeMapper.addLike(bno, username);
			boardMapper.updateLike(bno, 1);
		}
		else if(diff < 0){
			likeMapper.subLike(bno, username);
			boardMapper.updateLike(bno, -1);
		}
		else throw new BadLikeUpdateException();
	}

	/* 게시물 삭제는 정책적으로 불가능
	@Override
	@Transactional
	@Loggable
	public void remove(int bno) throws Exception {
		boardMapper.deleteAllAttach(bno);
		boardMapper.delete(bno);
	}
	*/
	
	@Override
	public List<BoardVO> listAll() throws Exception {
		return boardMapper.listAll();
	}

	@Override
	public List<BoardVO> listCriteria(Criteria cri) throws Exception {
		return boardMapper.listCriteria(cri).stream()
				.map(b -> {
					try {
						// 대댓글 갯수까지 포함한 총 댓글 수 
						b.setReplyCnt(replyMapper.readTotalCnt(b.getBno(), false));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return b;
				})
				.collect(Collectors.toList());
	}

	@Override
	public List<BoardVO> listByRegdate(LocalDateTime regdate) throws Exception {
		return boardMapper.listRegdate(regdate);
	}

	@Override
	public int getTotalCnt() throws Exception {
		return boardMapper.totalCount();
	}

	@Override
	public List<BoardVO> listSearch(SearchCriteria cri) throws Exception {
		return boardMapper.listSearch(cri).stream()
				.map(b -> {
					try {
						b.setReplyCnt(replyMapper.readTotalCnt(b.getBno(), false));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return b;
				})
				.collect(Collectors.toList());
	}

	@Override
	public int getSearchCnt(SearchCriteria cri) throws Exception {
		return boardMapper.searchCount(cri);
	}

	@Override
	public List<AttachVO> getAttach(int bno) throws Exception {
		return boardMapper.getAttach(bno);
	}

	@Override
	@Loggable
	public void removeAttach(String fullName) throws Exception {
		boardMapper.deleteAttach(fullName);
	}

	@Override
	public int getReplyCnt(int bno) throws Exception {
		return boardMapper.readReplyCnt(bno);
	}

	@Override
	public void updateViewCnt(int bno) throws Exception {
		boardMapper.updateViewCnt(bno);
	}

	@Override
	public void updateReportCnt(int bno, int diff) throws Exception {
		if(diff > 1) diff = 1;
		else if(diff < -1) diff = -1;
		else if(diff == 0) return;
		
		boardMapper.updateReportCnt(bno, diff);
	}	
	
	@Override
	public int getTotalCntByWriter(String username) throws Exception {
		return boardMapper.readTotalCntByWriter(username);
	}

	@Override
	public List<BoardVO> listByWriter(String writer, Criteria cri) throws Exception {
		return boardMapper.listBoardByWriter(writer, cri)
				.stream()
				.map(b -> {
					try {
						b.setReplyCnt(replyMapper.readTotalCnt(b.getBno(), false));
					} catch (Exception e) {
						e.printStackTrace();
					}
					return b;
				})
				.collect(Collectors.toList());
	}
	
}
