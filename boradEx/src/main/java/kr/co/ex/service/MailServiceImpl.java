package kr.co.ex.service;

import java.util.List;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import kr.co.ex.annoation.Loggable;
import kr.co.ex.dto.MailDto;

@Service
public class MailServiceImpl implements MailService {
	
	@Resource(name="javaMailSender")
	private JavaMailSender mailSender;
	
	@Override
	@Loggable
	public boolean send(MailDto dto){
		MimeMessage msges[] = dto.getToList().stream().map(receiver -> {
			MimeMessage msg = mailSender.createMimeMessage();
			try{
			MimeMessageHelper message = new MimeMessageHelper(msg, true, "UTF-8");
		    message.setFrom(dto.getFrom());
		    message.setTo(receiver);
		    message.setSubject(dto.getTitle());
		    message.setText(dto.getContent(), false);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		    return msg;
		}).toArray(MimeMessage[]::new);
		 mailSender.send(msges);
		 return true;
	}
	
	@Override
	@Loggable
	public boolean send(List<MailDto> dtos){
		MimeMessage msges[] = dtos.stream().map(dto -> {
			MimeMessage msg = mailSender.createMimeMessage();
			try{
			MimeMessageHelper message = new MimeMessageHelper(msg, true, "UTF-8");
		    message.setFrom(dto.getFrom());
		    message.setTo(dto.getTo());
		    message.setSubject(dto.getTitle());
		    message.setText(dto.getContent(), false);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		    return msg;
		}).toArray(MimeMessage[]::new);
		 mailSender.send(msges);
		 return true;
	}
}
