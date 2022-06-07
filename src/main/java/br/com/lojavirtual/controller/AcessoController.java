package br.com.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@ResponseBody
	@DeleteMapping(value = "/deletarAcessoPorId/{id}") /*Mapeando a URL pra receber um json*/
	public ResponseEntity<?> deletarAcessoPorId(@PathVariable("id") Long id) {
		
		acessoRepository.deleteById(id);
		
		return new ResponseEntity("**Acesso removido com sucesso pelo Id!**", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/obterAcessoPorId/{id}")
	public ResponseEntity<Acesso> obterAcessoPorId(@PathVariable("id") Long id) {
		
		
		Acesso acesso = acessoRepository.findById(id).get();
		
		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/buscarPorDescricao/{descricao}")
	public ResponseEntity<List<Acesso>> buscarPorDescricao(@PathVariable("descricao") String descricao) {		
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDescricao(descricao);
		
		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
	}

}
