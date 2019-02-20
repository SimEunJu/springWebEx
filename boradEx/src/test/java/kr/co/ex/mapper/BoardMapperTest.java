package kr.co.ex.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardMapperTest {

	@Setter(onMethod_ = {@Autowired})
	private BoardMapper boardMapper;
	
	@Test
	public void testBoardMapper() throws Exception{
		log.info("board mapper test");
		log.info(boardMapper.read(1));
		//.forEach(board -> log.info(board));
	}
}
