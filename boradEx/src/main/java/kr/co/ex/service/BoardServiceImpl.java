package kr.co.ex.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.annoation.Loggable;
import kr.co.ex.common.NoticeBoardControl;
import kr.co.ex.domain.AttachVO;
import kr.co.ex.domain.BoardVO;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.MsgVO;
import kr.co.ex.domain.SearchCriteria;
import kr.co.ex.exception.BadLikeUpdateException;
import kr.co.ex.mapper.BoardMapper;
import kr.co.ex.mapper.MsgMapper;
import kr.co.ex.mapper.ReplyMapper;
import kr.co.ex.mapper.UserLikeMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	@NonNull private BoardMapper boardMapper;
	@NonNull private ReplyMapper replyMapper;
	@NonNull private UserLikeMapper likeMapper;
	@NonNull private NoticeBoardControl notiControl;
	@NonNull private BCryptPasswordEncoder pwCrypt;
	@NonNull private MsgMapper msgMapper;
	
	@Override
	@Transactional
	public void register(BoardVO board) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<String> authorities = auth.getAuthorities().stream().map(a -> a.toString()).collect(Collectors.toList());
		if(authorities.contains("ROLE_ANONYMOUS")){
			board.setWriter("익명");
			board.setPassword(pwCrypt.encode(board.getPassword()));
		}
		else board.setWriter(auth.getName());
		
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
	public String getWriterName(int bno) {
		return boardMapper.readWriterName(bno);
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
				boardMapper.replaceAttach(bno, file);
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

	
	@Override
	@Transactional
	@Loggable
	public void removeNoti(int bno) throws Exception {
		try{
			boardMapper.deleteAllAttach(bno);
			boardMapper.delete(bno, "A");
			notiControl.releaseNotiBoardIdx(bno);
		}catch(Exception e){
			notiControl.rollbackReleaseNotiBoardIdx(bno);
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional
	@Loggable
	public void remove(BoardVO vo) throws Exception {
		// 만약 authority type이 더 늘어난다면 어떻게 될까?
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<String> authorities = auth.getAuthorities().stream().map(a -> a.toString()).collect(Collectors.toList());
		vo.setDeleteType("B");
		if(authorities.contains("ROLE_ANONYMOUS")){ 
			if(!matchPassword(vo.getBno(), vo.getPassword())) throw new AccessDeniedException("익명 게시글에 대한 비밀번호가 다릅니다.");
		}
		else if(authorities.contains("ROLE_ADMIN")){
			vo.setDeleteType("A");
			MsgVO msg = new MsgVO();
			BoardVO deleted = read(vo.getBno());
			msg.setReceiver(deleted.getWriter());
			// 관리자
			msg.setSender("관리자");
			String title = deleted.getTitle();
			String abbTitle = title.length() > 20 ? title.substring(0, 20)+"..." : title;
			msg.setTitle(abbTitle+" 해당 게시글은 삭제 처리되었습니다.");
			// 삭제 이유 작성
			// 삭제 게시글 확인 링크
			msg.setContent("삭제된 게시글 확인하기("+"http://localhost:8090/board/daily/"+vo.getBno()+"/temp)");
			msgMapper.createMsg(msg);
		}
		
		try{
			boardMapper.deleteAllAttach(vo.getBno());
			boardMapper.delete(vo.getBno(), vo.getDeleteType());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public void removeByWriter() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boardMapper.deleteByWriter(username);
	}
	
	@Override
	public boolean matchPassword(int bno, String raw) throws Exception{
		return pwCrypt.matches(raw, boardMapper.readPassword(bno));
	}
	
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
	public int getTotalCnt(Criteria cri) throws Exception {
		return boardMapper.totalCount(cri);
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
	public List<BoardVO> listNotice(NoticeBoardControl.NoticeBoardCriteria cri) throws Exception {
		return boardMapper.listNotice(cri);
	}

	@Override
	public int getNoticeCnt(NoticeBoardControl.NoticeBoardCriteria cri) throws Exception {
		return boardMapper.totalCount(cri);
	}
	
	@Override
	public void registerNotice(BoardVO vo){
		int bno = 0;
		try{
			bno = notiControl.requestNotiBoardIdx();
			vo.setBno(bno);
			vo.setWriter("관리자");
			boardMapper.createNotice(vo);
		}catch(Exception e){
			if(bno != 0) notiControl.rollbackRequestNotiBoardIdx(bno);
			e.printStackTrace();
		}	
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
	public int getTotalCntByWriter(String username, SearchCriteria cri) throws Exception {
		return boardMapper.readTotalCntByWriter(username, cri);
	}

	@Override
	public List<BoardVO> listByWriter(String writer, SearchCriteria cri) throws Exception {
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
