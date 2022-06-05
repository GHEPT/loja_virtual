package br.com.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.lojavirtual.controller.AcessoController;
import br.com.lojavirtual.model.Acesso;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualApplication.class)
public class LojaVirtualApplicationTests extends TestCase{
	
	@Autowired
	private AcessoController acessoController;
	
	@Test
	public void testeCadastraAcesso() {
		
		Acesso acesso = new Acesso();		
		acesso.setDescricao("ROLE_ADMIN");
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		assertEquals(true, acesso.getId() > 0);
	}
}
