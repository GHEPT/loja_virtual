package br.com.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.model.Usuario;
import br.com.lojavirtual.repository.PessoaRepository;
import br.com.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SendEmailService sendEmailService;
	
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pj) {
		
		for (int i = 0; i< pj.getEnderecos().size(); i++) {
			pj.getEnderecos().get(i).setPessoa(pj);
			pj.getEnderecos().get(i).setEmpresa(pj);
		}
		
		pj = pessoaRepository.save(pj);
		
		Usuario usuarioPj = usuarioRepository.findUserByPessoa(pj.getId(), pj.getEmail());
		
		if (usuarioPj == null) {
			String constraint = usuarioRepository.consultaConstraintAcesso();
			if (constraint != null) {
				jdbcTemplate.execute("begin; alter table usuario_acesso drop constraint " + constraint + "; commit;");				
			}
			
			usuarioPj = new Usuario();
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(pj);
			usuarioPj.setPessoa(pj);
			usuarioPj.setLogin(pj.getEmail());
			
			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			
			usuarioPj.setSenha(senhaCript);
			usuarioPj = usuarioRepository.save(usuarioPj);
			
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId());
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");
			
			StringBuilder mensagemHtml = new StringBuilder();
			mensagemHtml.append("<b>Segue abaixo seus dados para acesso a Loja Virtual</b><br/><br/>");
			mensagemHtml.append("<b>Login: </b>" + pj.getEmail()+ "<br/");
			mensagemHtml.append("<b>Senha: </b>" + senha + "<br/><br/>");
			mensagemHtml.append("Obrigado!");
			
			
			try {
				sendEmailService.enviarEmailHtml("Acesso gerado para Loja Virtual", mensagemHtml.toString(), pj.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return pj;	
		
	}
}
