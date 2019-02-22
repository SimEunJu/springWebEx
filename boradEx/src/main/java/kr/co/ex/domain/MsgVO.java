package kr.co.ex.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MsgVO {
	private Integer msgNo;
	private String title;
	private String content;
	private String sender;
	private String receiver;
	private boolean senderDeleteFlag;
	private boolean receiverDeleteFlag;
	private LocalDateTime regdate;
}
