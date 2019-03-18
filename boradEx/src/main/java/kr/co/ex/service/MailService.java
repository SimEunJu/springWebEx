package kr.co.ex.service;

import java.util.List;

import kr.co.ex.dto.MailDto;

public interface MailService {
	public boolean send(MailDto dto);
	public boolean send(List<MailDto> dtos);
}
