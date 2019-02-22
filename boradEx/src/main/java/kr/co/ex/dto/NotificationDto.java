package kr.co.ex.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class NotificationDto {
	private Integer nno;
	private LocalDateTime regdate;
	private LocalDateTime readdate;
	private boolean readFlag;
	private boolean deleteFlag;
	private Integer bno;
	private Integer rno;
	private String username;
	private char type;
	
	private String title;
	private String reply;
}
