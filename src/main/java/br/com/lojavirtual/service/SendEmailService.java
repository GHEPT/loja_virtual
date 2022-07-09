package br.com.lojavirtual.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {
	
	private String usuario = "teodoro.edu@hotmail.com";
	private String senha = "Rg42558656@";
	
	@Async
	public void enviarEmailHtml(String assunto, String mensagem, String emailDestino) throws UnsupportedEncodingException, MessagingException {
		
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp-mail.outlook.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.socketFactory.port", "587");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");				
		
		
		Authenticator authenticator = new Authenticator() {			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {				
				return new PasswordAuthentication(usuario, senha);
			}
		};
		
		Session session = Session.getDefaultInstance(properties, authenticator);
		
		session.setDebug(true);
		
		try {
			
			Address[] toUser = InternetAddress.parse(emailDestino);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(usuario, "Eduardo - Loja Virtual", "UTF-8"));
			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject(assunto);
			message.setContent(mensagem, "text/html; charset=utf-8");
			
			Transport.send(message);	
			
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}			
	}
}
