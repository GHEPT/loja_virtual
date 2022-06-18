package br.com.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import br.com.lojavirtual.model.PessoaFisica;
import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.repository.PessoaRepository;
import br.com.lojavirtual.service.PessoaUserService;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Test
	public void testCadPessoaFisica() {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("12345678000110");
		pessoaJuridica.setNome("Dono Empresa");
		pessoaJuridica.setNomeFantasia("Nome Fantasia");
		pessoaJuridica.setInscEstadual("123.456.58");
		pessoaJuridica.setInscMunicipal("");
		pessoaJuridica.setTelefone("11987898789");
		pessoaJuridica.setEmail("empresa@gmail.com");
		pessoaJuridica.setRazaoSocial("Razão Social LTDA");

		pessoaRepository.save(pessoaJuridica);
		
		/*
		 * PessoaFisica pessoaFisica = new PessoaFisica();
		 * 
		 * pessoaFisica.setCpf("12345678910"); pessoaFisica.setNome("Eduardo");
		 * pessoaFisica.setEmail("eduardo@gmail.com");
		 * pessoaFisica.setTelefone("11998789878");
		 * pessoaFisica.setEmpresa(pessoaFisica);
		 */
	}
}
