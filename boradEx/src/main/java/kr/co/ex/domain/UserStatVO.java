package kr.co.ex.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserStatVO {

	private LocalDateTime today;
	private int joinCnt;
	private int leaveCnt;
	private int postCnt;
	private int visitCnt;
}
