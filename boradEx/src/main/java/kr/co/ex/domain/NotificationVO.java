package kr.co.ex.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Builder
public class NotificationVO {
	private Integer nno;
	private LocalDateTime regdate;
	private LocalDateTime readdate;
	private boolean readFlag;
	private boolean deleteFlag;
	private Integer bno;
	private Integer rno;
	private String username;
	private char type;

}
