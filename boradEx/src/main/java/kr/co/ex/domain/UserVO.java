package kr.co.ex.domain;

import java.util.Date;

public class UserVO {
	private String uid;
	private String upw;
	private String uname;
	private Integer upoint;
	private String sessionkey;
	private Date sessionlimit;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUpw() {
		return upw;
	}
	public void setUpw(String upw) {
		this.upw = upw;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public Integer getUpoint() {
		return upoint;
	}
	public void setUpoint(Integer upoint) {
		this.upoint = upoint;
	}
	
	public String getSessionkey() {
		return sessionkey;
	}
	public void setSessionkey(String sessionkey) {
		this.sessionkey = sessionkey;
	}
	public Date getSessionlimit() {
		return sessionlimit;
	}
	public void setSessionlimit(Date sessionlimit) {
		this.sessionlimit = sessionlimit;
	}
	
	@Override
	public String toString() {
		return "UserVO [uid=" + uid + ", upw=" + upw + ", uname=" + uname + ", upoint=" + upoint + ", sessionkey="
				+ sessionkey + ", sessionlimit=" + sessionlimit + "]";
	}
}
