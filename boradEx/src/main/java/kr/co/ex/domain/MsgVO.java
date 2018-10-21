package kr.co.ex.domain;
import java.util.Date;

public class MsgVO {
	private Integer mid;
	private String targetid;
	private String sender;
	private String msg;
	private Date opendate;
	private Date senddate;
	public Integer getMid() {
		return mid;
	}
	public void setMid(Integer mid) {
		this.mid = mid;
	}
	public String getTargetid() {
		return targetid;
	}
	public void setTargetid(String targetid) {
		this.targetid = targetid;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Date getOpendate() {
		return opendate;
	}
	public void setOpendate(Date opendate) {
		this.opendate = opendate;
	}
	public Date getSenddate() {
		return senddate;
	}
	public void setSenddate(Date senddate) {
		this.senddate = senddate;
	}
	@Override
	public String toString() {
		return "MsgVO [mid=" + mid + ", targetid=" + targetid + ", sender=" + sender + ", msg=" + msg + ", opendate="
				+ opendate + ", senddate=" + senddate + "]";
	}
	
	
}
