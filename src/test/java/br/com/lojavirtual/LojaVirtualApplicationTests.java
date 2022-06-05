package br.com.lojavirtual;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.lojavirtual.controller.AcessoController;
import br.com.lojavirtual.model.Acesso;
import br.com.lojavirtual.repository.AcessoRepository;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualApplication.class)
public class LojaVirtualApplicationTests extends TestCase{
	
	@Autowired
	private AcessoController acessoController;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@Test
	public void testeCadastraAcesso() {
		
		Acesso acesso = new Acesso();		
		acesso.setDescricao("ROLE_ADMIN");
		
		assertEquals(true, acesso.getId() == null);
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		/*Está gravando no banco de dados e retornando um ID?*/
		assertEquals(true, acesso.getId() > 0);
		
		/*Validando dados salvos de forma correta*/
		assertEquals("ROLE_ADMIN", acesso.getDescricao());
		
		/*Teste de carregamento*/
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();		
		assertEquals(acesso.getId(), acesso2.getId());
		
		/*Teste de delete*/
		acessoRepository.deleteById(acesso2.getId());
		acessoRepository.flush();
		
		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);
		assertEquals(true, acesso3 == null);
		
		/*Teste de query*/
		acesso = new Acesso();
		acesso.setDescricao("ROLE_ALUNO");
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		List<Acesso> acessos = acessoRepository.buscarAcessoDescricao("ALUNO".trim().toUpperCase());
		assertEquals(1, acessos.size());
		
		acessoRepository.deleteById(acesso.getId());
	}
}
