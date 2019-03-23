package kr.co.ex.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class NoticeCriteria extends Criteria {
	private final int NOTICE_BOARD_START = 20;
	private final int NOTICE_BOARD_END = 50;
}
