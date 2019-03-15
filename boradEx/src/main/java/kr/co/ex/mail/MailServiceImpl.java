package kr.co.ex.mail;

import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
	
	@NonNull
	private JavaMailSender mailSender;
	
	@Override
	public boolean send(MailDto dto){
		MimeMessage msges[] = dto.getTo().stream().map(receiver -> {
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
}
