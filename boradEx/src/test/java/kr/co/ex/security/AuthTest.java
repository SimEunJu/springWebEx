package kr.co.ex.security;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/security-context.xml"
})
@Log4j2
public class AuthTest {
	
	@Setter(onMethod_ = @Autowired)
	private DataSource ds;
	
	@Test
	public void testPasswordEncoder(){
		String sql = "insert into tbl_member_auth(userid, auth) values(?,?)";
		for(int i=0; i<3; i++){
			Connection con = null;
			PreparedStatement state = null;
			try{
				con = ds.getConnection();
				state = con.prepareStatement(sql);
				if(i == 0){
					state.setString(1, "user");
					state.setString(2, "ROLE_USER");
				}else if(i == 1){
					state.setString(1, "manager");
					state.setString(2, "ROLE_MANAGER");
				}else if(i == 2){
					state.setString(1, "admin");
					state.setString(2, "ROLE_ADMIN");
				}
				state.executeUpdate();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(state != null){
					try{state.close();}
					catch(Exception e){}
				}
				if(con != null){
					try{con.close();}
					catch(Exception e){}
				}
			}
		}
	}
}
