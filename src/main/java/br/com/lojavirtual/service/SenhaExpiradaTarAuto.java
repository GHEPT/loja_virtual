package br.com.lojavirtual.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.lojavirtual.model.Usuario;
import br.com.lojavirtual.repository.UsuarioRepository;

@Component
@Service
public class SenhaExpiradaTarAuto {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private SendEmailService sendEmailService;

	//@Scheduled(initialDelay = 5000, fixedDelay = 86400000) // A cada 24 horas
	@Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo") // Vai rodar todo dia às 11h da manhã, horário de Brasília.
	public void notificarTrocaSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {
		
		List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();
		
		for (Usuario usuario: usuarios) {
			StringBuilder msg = new StringBuilder();
			msg.append("Olá, ").append(usuario.getPessoa().getNome()).append("!<br/><br/>");
			msg.append("Sua senha expirou e você precisa trocá-la para continuar acessando a loja.").append("<br/>");
			msg.append("Por favor, defina uma nova senha no site da Loja Virtual.").append("<br/><br/>");
			msg.append("Atenciosamente,").append("<br/>");
			msg.append("Eduardo - Loja Virtual").append("<br/>");
			
			sendEmailService.enviarEmailHtml("Troca de Senha", msg.toString(), usuario.getLogin());
			
			Thread.sleep(3000);
			
		}
	}
	
}
