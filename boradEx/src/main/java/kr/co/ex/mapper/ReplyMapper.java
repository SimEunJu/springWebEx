package kr.co.ex.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.ex.domain.Criteria;
import kr.co.ex.domain.ReplyVO;
import kr.co.ex.dto.ReplyDto;

@Mapper
public interface ReplyMapper {
	public void create(ReplyVO vo) throws Exception;
	public List<ReplyVO> list(Integer bno) throws Exception;
	public List<ReplyDto> listCriteria(@Param("bno") Integer bno, @Param("cri")Criteria cri) throws Exception;
	public List<ReplyVO> listCriteriaAdded(@Param("parRno") Integer parRno, @Param("cri") Criteria cri) throws Exception;
	public List<ReplyVO> listReplyByReplyer(@Param("replyer") String replyer, @Param("cri") Criteria cri) throws Exception;

	public int totalCount(@Param("bno") int bno, @Param("notIncludeAdded") boolean notIncludeAdded) throws Exception;
	public int addedTotalCount(int parRno) throws Exception;
	public int readTotalCntByReplyer(String username);
	
	public String getReplyer(int parRno) throws Exception;
	public ReplyVO get(int rno) throws Exception;
	
	public void update(ReplyVO vo) throws Exception;
	public void updateReport(int rno) throws Exception;
	
	public void delete(@Param("deleteType") String deleteType, @Param("rno") Integer rno) throws Exception;
	public void deleteReplies(@Param("deleteType") String deleteType, @Param("rno") List<Integer> rno) throws Exception;
}
