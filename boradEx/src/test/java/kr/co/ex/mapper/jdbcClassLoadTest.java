package kr.co.ex.mapper;

import java.sql.DriverManager;
import java.util.Enumeration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mysql.cj.xdevapi.Statement;

import lombok.extern.log4j.Log4j2;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class jdbcClassLoadTest {

	@Test
	public void testBoardMapper() throws Exception{
		String driver = "com.mysql.cj.jdbc.Driver";
		Statement statement = null; 
		Class.forName(driver); 
		Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
		while(drivers.hasMoreElements()){
			log.info(drivers.nextElement().toString());
		}
	}
}
