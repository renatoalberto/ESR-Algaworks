package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.model.input.CidadeInputDTO;
import com.algaworks.algafood.api.model.output.CidadeDTO;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaExecption;
import com.algaworks.algafood.domain.exception.NegocioExecption;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CidadeCadastroService;


@RestController
@RequestMapping("/cidades") 
public class CidadeController {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CidadeCadastroService cidadeCadastro;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;
	
	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;
	
	@GetMapping
	public List<CidadeDTO> listar() {
		return cidadeModelAssembler.toCollectionDTO(cidadeRepository.findAll());
	}
	
	@GetMapping("/{cidadeId}")
	public CidadeDTO buscar(@PathVariable Long cidadeId) {
		return cidadeModelAssembler.toDTO(cidadeCadastro.buscarOuFalhar(cidadeId));
	}
	
//	@PostMapping
//	public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
//		try {
//			cidade = cidadeCadastro.salvar(cidade);
//			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
//		} catch (EstadoNaoEncontradaExecption e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeDTO adicionar(@RequestBody @Valid CidadeInputDTO cidadeInput) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainCidade(cidadeInput);
			return cidadeModelAssembler.toDTO(cidadeCadastro.salvar(cidade));
		} catch (EstadoNaoEncontradaExecption e) {
			throw new NegocioExecption(e.getMessage(), e);
		}
	}
	
	
	@PutMapping("/{cidadeId}")
	public CidadeDTO atualizar(@PathVariable long cidadeId, @RequestBody @Valid CidadeInputDTO cidadeInput) {
		Cidade cidadeAtual = cidadeCadastro.buscarOuFalhar(cidadeId);
		
//		BeanUtils.copyProperties(cidade, cidadeAtual, "id");
		cidadeInputDisassembler.copyToDomainCidade(cidadeInput, cidadeAtual);
		
		try {
			return cidadeModelAssembler.toDTO(cidadeCadastro.salvar(cidadeAtual));			
		} catch (EstadoNaoEncontradaExecption e) {
			throw new NegocioExecption(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long cidadeId) {
		cidadeCadastro.excluir(cidadeId);
	}

}
