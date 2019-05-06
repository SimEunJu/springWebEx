package kr.co.ex.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j2;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/security-context.xml"
})
@Log4j2
public class PasswordMatchTest {
	
	@Autowired private BCryptPasswordEncoder encoder;
	@Autowired private DataSource ds;	
	
	@Test
	public void testPasswordMath(){
		String sql = "select password from tbl_board where bno=?";
		Connection con = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try{
			con = ds.getConnection();
			stat = con.prepareStatement(sql);
			stat.setInt(1, 128);
			res = stat.executeQuery();
			res.next();
			String password = res.getString(1);
			log.info(encoder.encode("1234"));
			log.info(encoder.encode("1234"));
			log.info(encoder.encode("1234"));
			log.info(encoder.encode("1234"));
			if(encoder.matches("1234", password)) log.info("equal password");
			//$2a$10$jOizLXoUMlSV8oKrgrfEPehfVy9MpFicTEMUUlWLj8g4D.u.hbeji
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
	}
}
