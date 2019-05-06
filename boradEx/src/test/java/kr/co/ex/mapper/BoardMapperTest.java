package kr.co.ex.mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/security-context.xml"})
@Log4j2
public class BoardMapperTest {

	@Setter(onMethod_ = {@Autowired})
	private BoardMapper boardMapper;
	@Autowired private DataSource ds;
	
	@Test
	public void testBoardMapper() throws ClassNotFoundException, SQLException{
/*		Class.forName("com.mysql.cj.jdbc.Driver");
		      Connection con = DriverManager.getConnection ( 
		         "jdbc:mysql://localhost:3306/boardex?userSSL=false&serverTimezone=Asia/Seoul&allowMultiQueries=true","boardex","boardex");
		      PreparedStatement stmt = con.prepareStatement(
		         "select count(bno) from tbl_board");
		      
		      ResultSet rs =  stmt.executeQuery();
		      
		      while (rs.next()) {
		    	  log.info("result");
		    	  log.info(rs.getInt(1));
		      }*/
		String sql = "select count(bno) from tbl_board";
		Connection con = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try{
			con = ds.getConnection();
			stat = con.prepareStatement(sql);
			stat.execute();
			res = stat.getResultSet();
			if(res.next()){
				log.info(res.getInt(1));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(res != null){
				try{ res.close(); }
				catch(Exception e){}
			}
			if(stat != null){
				try{ stat.close(); }
				catch(Exception e){}
			}
			if(con != null){
				try{ con.close(); }
				catch(Exception e){}
			}
		}
		
		/*try{
			log.info("board mapper test");
			log.info(boardMapper.searchCount(new SearchCriteria()));
		}
		catch(Exception e){
			e.printStackTrace();
		}*/
	}
}
