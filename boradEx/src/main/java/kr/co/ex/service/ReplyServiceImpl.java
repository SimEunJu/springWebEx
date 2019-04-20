package kr.co.ex.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ex.annoation.Loggable;
import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.dto.ReplyDto;
import kr.co.ex.mapper.BoardMapper;
import kr.co.ex.mapper.ReplyMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	@NonNull private ReplyMapper replyMapper;
	@NonNull private BoardMapper boardMapper;
	@NonNull private BCryptPasswordEncoder pwEncoder;
	
	@Override
	public void addReply(ReplyVO vo) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<String> authorities = auth.getAuthorities().stream().map(a -> a.toString()).collect(Collectors.toList());
		if(authorities.contains("ROLE_ANONYMOUS")){
			vo.setReplyer("익명");
			vo.setPassword(pwEncoder.encode(vo.getPassword()));
		}
		else vo.setReplyer(auth.getName());
		replyMapper.create(vo);
	}
	
	@Override
	public List<ReplyVO> listReply(int bno) throws Exception {	
		return replyMapper.list(bno).stream().map(r -> {
			if(r.isSecret()){
				r.setReplyer(null);
				r.setReply(null);
			}
			return r;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<ReplyDto> listCriteriaReply(int bno, Criteria cri) throws Exception {
		return filterReplies(getWriterName(bno), replyMapper.listCriteria(bno, cri), true);
	}
	
	private List<ReplyDto> filterReplies(String owner, List<ReplyDto> list, boolean setAddedCnt){
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		if(owner.equals(user)){
			return list
					.stream().map(r -> {
						
						r.setSecret(false);
						if(r.getDeleteType() != null){
							r.setDeleteFlag(true);
							setReplyNull(r);
						}
						
						try {
							if(setAddedCnt) r.setAddedCount(getAddedTotalCount(r.getRno()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						return r;
					}).collect(Collectors.toList());
		}
		
		return list.stream().map(r -> {
			if(r.isSecret()) setReplyNull(r);
			if(r.getDeleteType() !=  null){
				r.setDeleteFlag(true);
				setReplyNull(r);
			}
			
			try {
				r.setAddedCount(getAddedTotalCount(r.getRno()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return r;
		}).collect(Collectors.toList());
	}
	
	private void setReplyNull(ReplyVO reply){
		reply.setReply(null);
		reply.setReplyer(null);
	}
	
	@Override
	public List<ReplyDto> listCriteriaAddedReply(int bno, int parRno, Criteria cri) throws Exception {
		return filterReplies(getWriterName(bno), replyMapper.listCriteriaAdded(parRno, cri), false);
	}
	
	@Override
	public List<ReplyVO> listReplyByWriter(Criteria cri) throws Exception {
		String replyer = SecurityContextHolder.getContext().getAuthentication().getName();
		return replyMapper.listReplyByReplyer(replyer, cri);
	}
	
	@Override
	public List<ReplyVO> listReplyByReportCnt(Criteria cri) throws Exception {
		return replyMapper.listReplyByReportCnt(cri);
	}
	
	private String getWriterName(int bno){
		return boardMapper.readWriterName(bno);
	}
	
	@Override
	public String getReplyer(int parBno) throws Exception {
		return boardMapper.readWriterName(parBno);
	}

	@Override
	public int getTotalCount(int bno, boolean notIncludeAdded) throws Exception {
		return replyMapper.readTotalCnt(bno, notIncludeAdded);
	}

	@Override
	public int getAddedTotalCount(int parRno) throws Exception {
		return replyMapper.readAddedTotalCnt(parRno);
	}

	@Override
	public int getTotalCntByReplyer(String username) throws Exception {
		return replyMapper.readTotalCntByReplyer(username);
	}
	
	@Override
	public ReplyVO getReply(int rno) throws Exception {
		return replyMapper.read(rno);
	}

	@Override
	public void modifyReply(ReplyVO vo) throws Exception {
		replyMapper.update(vo);
	}

	@Override
	@Transactional
	@Loggable
	public String removeReply(int rno, int bno) throws Exception {
		String deleteType = getDeleteType(rno, getWriterName(bno));
		replyMapper.delete(deleteType, rno);
		return deleteType;
	}

	@Override
	@Transactional
	@Loggable
	public String removeAnonymousReply(int rno, int bno, String password) throws Exception{
		String deleteType = null;
		try{
			deleteType = getDeleteType(rno, getWriterName(bno));
		}catch(AccessDeniedException e){
			if(pwEncoder.matches(password, getPassword(rno))) deleteType = "R";
			else throw new AccessDeniedException("익명 댓글에 대한 비밀번호가 다릅니다.");
		}
		replyMapper.delete(deleteType, rno);
		return deleteType;
	}
	
	private String getPassword(int rno) throws Exception{
		return replyMapper.readPassword(rno);
	}
	
	@Override
	@Loggable
	public void removeReplies(List<Integer> rno, int bno) throws Exception {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		String deleteType = getDeleteType(rno.get(0), getWriterName(bno));
		replyMapper.deleteReplies(deleteType, rno);
	}
	
	@Override
	@Loggable
	public void removeReplies(List<Integer> rno) throws Exception {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		String deleteType = getDeleteType(rno.get(0), null);
		replyMapper.deleteReplies(deleteType, rno);
	}
	
	private String getDeleteType(int rno, String boardWriter) throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<String> roles = auth.getAuthorities().stream()
				.map(a -> a.getAuthority()).collect(Collectors.toList());
		String user = auth.getName();
	
		if(roles.contains("ROLE_ADMIN")) user = "ADMIN";
		else if(!roles.contains("ROLE_USER")) throw new AccessDeniedException("USER 권한 이상의 권한을 가지고 있지 않습니다.");
		
		String deleteType = "";
		if(user.equals(boardWriter)) deleteType = "B";
		else if(user.equals(getReplyer(rno))) deleteType = "R";
		else if(user.equals("ADMIN")) deleteType = "A";
		
		return deleteType;
	}

	@Override
	public void removeReplyByReplyer() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		replyMapper.deleteByReplyer(username);
	}

	@Override
	public void reportReply(int rno) throws Exception {
		replyMapper.updateReport(rno);
	}

}
