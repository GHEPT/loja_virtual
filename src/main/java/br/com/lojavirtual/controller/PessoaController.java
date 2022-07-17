package br.com.lojavirtual.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.lojavirtual.ExceptionLojaVirtual;
import br.com.lojavirtual.model.PessoaFisica;
import br.com.lojavirtual.model.PessoaJuridica;
import br.com.lojavirtual.repository.PessoaFisicaRepository;
import br.com.lojavirtual.repository.PessoaRepository;
import br.com.lojavirtual.service.PessoaUserService;
import br.com.lojavirtual.util.ValidaCNPJ;
import br.com.lojavirtual.util.ValidaCPF;

@RestController
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	
	@ResponseBody
	@PostMapping(value = "/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionLojaVirtual{
		
		if (pessoaJuridica == null) {
			throw new ExceptionLojaVirtual("Pessoa Jurídica não pode ser NULL");			
		}
		
		//CNPJ Cadastrado?
		if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionLojaVirtual("Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());
		}
		
		//CNPJ Inválido?
		if (pessoaJuridica.getId() == null && ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj()) == false) {
			throw new ExceptionLojaVirtual("O CNPJ informado não é válido: " + pessoaJuridica.getCnpj());			
		}
		
		//Inscrição Estadual Cadastrada?
		if (pessoaJuridica.getId() == null && pessoaRepository.existInscrEstadualCadastrada(pessoaJuridica.getInscEstadual()) != null) {
			throw new ExceptionLojaVirtual("Já existe Inscrição Estadual cadastrada com o número: " + pessoaJuridica.getInscEstadual());
		}		
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@PostMapping(value = "/salvarPf")
	public ResponseEntity<PessoaFisica> salvarPf(@RequestBody @Valid PessoaFisica pessoaFisica) throws ExceptionLojaVirtual  {
		
		if (pessoaFisica == null) {
			throw new ExceptionLojaVirtual("Pessoa Física não pode ser NULL");			
		}
		
		//CPF Cadastrado?
		if (pessoaFisica.getId() == null && pessoaFisicaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new ExceptionLojaVirtual("Já existe CPF cadastrado com o número: " + pessoaFisica.getCpf());
		}
		
		//CPF Inválido?
		if (pessoaFisica.getId() == null && ValidaCPF.isCPF(pessoaFisica.getCpf()) == false) {
			throw new ExceptionLojaVirtual("O CPF informado não é válido: " + pessoaFisica.getCpf());			
		}
		
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}

}
