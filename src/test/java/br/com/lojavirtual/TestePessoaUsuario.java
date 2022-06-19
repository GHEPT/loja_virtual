package br.com.lojavirtual;

import java.util.Calendar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import br.com.lojavirtual.controller.PessoaController;
import br.com.lojavirtual.model.PessoaJuridica;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaController pessoaController;

	@Test
	public void testCadPessoaFisica() throws ExceptionLojaVirtual {

		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Dono Empresa");
		pessoaJuridica.setNomeFantasia("Nome Fantasia");
		pessoaJuridica.setInscEstadual("123.456.58");
		pessoaJuridica.setInscMunicipal("");
		pessoaJuridica.setTelefone("11987898789");
		pessoaJuridica.setEmail("empresa@gmail.com");
		pessoaJuridica.setRazaoSocial("Razão Social LTDA");

		pessoaController.salvarPj(pessoaJuridica);

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
