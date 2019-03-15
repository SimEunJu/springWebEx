package kr.co.ex.mail;

import java.util.List;

public interface MailService {
	public boolean send(MailDto dto);
	public boolean send(List<MailDto> dtos);
}
