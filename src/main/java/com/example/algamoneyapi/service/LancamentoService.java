package com.example.algamoneyapi.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.algamoneyapi.model.Lancamento;
import com.example.algamoneyapi.model.Pessoa;
import com.example.algamoneyapi.repository.LancamentoRepository;
import com.example.algamoneyapi.repository.PessoaRepository;
import com.example.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	

	@Autowired
	private PessoaRepository pessoaRepository;
	
	/*Tratamento para salvar um lancamento. Irá salvar um novo lançamento caso haja um 
	 * codigo de pessoa existente ou ativa*/
	public Lancamento salvar(@Valid Lancamento lancamento) {
		
		Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
		if(!pessoa.isPresent() || pessoa.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoRepository.save(lancamento);
	}
	
	//Metodo para implementar atualizacao de cadastro de Lancamento 
	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		if(!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento);
		}
		
		//ele ira altera a informacoes do Lancamento com excecao do codigo do lancamento
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		
		//retorno alteracao das informacoes em Lancamento
		return lancamentoRepository.save(lancamentoSalvo);
		
	}

	//valida as informacao da pessoa que esta solicitando alteracao
	private void validarPessoa(Lancamento lancamento) {
		Optional<Pessoa> pessoa = null;
		if (lancamento.getPessoa().getCodigo()!= null) {
			pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
		}
		
		if (!pessoa.isPresent()) { // isPresent() verifica se há um objeto pessoa
			throw new PessoaInexistenteOuInativaException(); // se não houver lanca uma exceçao
		}

	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
	
		Optional<Lancamento> lancamentoSalvo = lancamentoRepository.findById(codigo);
		if (lancamentoSalvo == null) {
			throw new IllegalArgumentException();
			
		}
		
		// se o valor estiver presente, retorna o valor , senao lanca uma excecao
		return lancamentoSalvo.orElseThrow(() -> new IllegalArgumentException());
	}


}
