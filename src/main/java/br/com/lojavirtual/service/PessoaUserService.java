package br.com.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.lojavirtual.model.PessoaFisica;
import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.model.Usuario;
import br.com.lojavirtual.repository.PessoaFisicaRepository;
import br.com.lojavirtual.repository.PessoaRepository;
import br.com.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private SendEmailService sendEmailService;

	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pj) {

		for (int i = 0; i < pj.getEnderecos().size(); i++) {
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

			usuarioRepository.insereAcessoUser(usuarioPj.getId());
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");

			StringBuilder mensagemHtml = new StringBuilder();
			mensagemHtml.append("<b>Segue abaixo seus dados para acesso a Loja Virtual</b><br/><br/>");
			mensagemHtml.append("<b>Login: </b>" + pj.getEmail() + "<br/");
			mensagemHtml.append("<b>Senha: </b>" + senha + "<br/><br/>");
			mensagemHtml.append("Obrigado!");

			try {
				sendEmailService.enviarEmailHtml("Acesso gerado para Loja Virtual", mensagemHtml.toString(),
						pj.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return pj;

	}

	public PessoaFisica salvarPessoaFisica(PessoaFisica pf) {

		for (int i = 0; i < pf.getEnderecos().size(); i++) {
			pf.getEnderecos().get(i).setPessoa(pf);
			//pf.getEnderecos().get(i).setEmpresa(pf);
		}

		pf = pessoaFisicaRepository.save(pf);

		Usuario usuarioPf = usuarioRepository.findUserByPessoa(pf.getId(), pf.getEmail());

		if (usuarioPf == null) {
			String constraint = usuarioRepository.consultaConstraintAcesso();
			if (constraint != null) {
				jdbcTemplate.execute("begin; alter table usuario_acesso drop constraint " + constraint + "; commit;");
			}

			usuarioPf = new Usuario();
			usuarioPf.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPf.setEmpresa(pf.getEmpresa());
			usuarioPf.setPessoa(pf);
			usuarioPf.setLogin(pf.getEmail());

			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);

			usuarioPf.setSenha(senhaCript);
			usuarioPf = usuarioRepository.save(usuarioPf);

			usuarioRepository.insereAcessoUser(usuarioPf.getId());

			StringBuilder mensagemHtml = new StringBuilder();
			mensagemHtml.append("<b>Segue abaixo seus dados para acesso a Loja Virtual</b><br/><br/>");
			mensagemHtml.append("<b>Login: </b>" + pf.getEmail() + "<br/");
			mensagemHtml.append("<b>Senha: </b>" + senha + "<br/><br/>");
			mensagemHtml.append("Obrigado!");

			try {
				sendEmailService.enviarEmailHtml("Acesso gerado para Loja Virtual", mensagemHtml.toString(),
						pf.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return pf;

	}

}
