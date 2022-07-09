package br.com.lojavirtual;

import java.util.Calendar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import br.com.lojavirtual.controller.PessoaController;
import br.com.lojavirtual.enums.TipoEndereco;
import br.com.lojavirtual.model.Endereco;
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

		Endereco endereco1 = new Endereco();
		endereco1.setRuaLogra("Rua nome da rua");
		endereco1.setNumero("1000");
		endereco1.setBairro("Jardim Bairro");
		endereco1.setComplemento("Frente");
		endereco1.setCidade("Cidade de Deus");
		endereco1.setUf("SP");
		endereco1.setCep("04547004");
		endereco1.setEmpresa(pessoaJuridica);
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		
		Endereco endereco2 = new Endereco();
		endereco2.setRuaLogra("Rua outro nome agora");
		endereco2.setNumero("40");
		endereco2.setBairro("Primavera");
		endereco2.setComplemento("B");
		endereco2.setCidade("Maranduba");
		endereco2.setUf("CA");
		endereco2.setCep("04500000");
		endereco2.setEmpresa(pessoaJuridica);
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		
		pessoaJuridica.getEnderecos().add(endereco2);
		pessoaJuridica.getEnderecos().add(endereco1);
		
		pessoaJuridica = pessoaController.salvarPj(pessoaJuridica).getBody();
		
		assertEquals(true, pessoaJuridica.getId() > 0);
		
		for (Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaJuridica.getEnderecos().size());

		
	}
}
