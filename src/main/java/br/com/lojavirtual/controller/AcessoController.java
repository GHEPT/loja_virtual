package br.com.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.lojavirtual.model.Acesso;
import br.com.lojavirtual.repository.AcessoRepository;
import br.com.lojavirtual.service.AcessoService;

@Controller
@RestController
public class AcessoController {
	
	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@ResponseBody /*pra poder dar retorno da API*/
	@PostMapping(value = "/salvarAcesso") /*Mapeando a URL pra receber um json*/
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) { /* O @RequestBody recebe o json e converte para objeto*/
		
		Acesso acessoSalvo = acessoService.save(acesso); 
		
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}
	
	
	@ResponseBody /*pra poder dar retorno da API*/
	@PostMapping(value = "/deletarAcesso") /*Mapeando a URL pra receber um json*/
	public ResponseEntity<?> deletarAcesso(@RequestBody Acesso acesso) { /* O @RequestBody recebe o json e converte para objeto*/
		
		acessoRepository.deleteById(acesso.getId());
		
		return new ResponseEntity("**Acesso removido com sucesso!**", HttpStatus.OK);
	}

}
