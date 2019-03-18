package kr.co.ex.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MailDto {
	private String from;
	private List<String> toList;
	private String to;
	private String title;
	private String content;
}
