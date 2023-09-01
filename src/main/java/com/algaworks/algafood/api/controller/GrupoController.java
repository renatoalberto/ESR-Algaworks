package com.algaworks.algafood.api.controller;

import java.util.Collection;

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

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.model.input.GrupoInputDTO;
import com.algaworks.algafood.api.model.output.GrupoDTO;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.GrupoCadastroService;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private GrupoCadastroService grupoCadastro;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@Autowired
	private GrupoInputDisassembler grupoInputDisassembler;
	
	@GetMapping
	public Collection<GrupoDTO> listar() {
		return grupoModelAssembler.collectionToDTO(grupoRepository.findAll());
	}
	
	@GetMapping("/{grupoId}")
	public GrupoDTO buscar(@PathVariable Long grupoId) {
		return grupoModelAssembler.toGrupoDTO(grupoCadastro.buscarOuFalhar(grupoId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoDTO adicionar(@RequestBody @Valid GrupoInputDTO grupoInput) {
		Grupo grupo = grupoInputDisassembler.toDomainGrupo(grupoInput);
		return grupoModelAssembler.toGrupoDTO(grupoCadastro.salvar(grupo)); 		
	}
	
	@PutMapping("/{grupoId}")
	public GrupoDTO adicionar(@PathVariable Long grupoId, @RequestBody @Valid GrupoInputDTO grupoInput) {		
		Grupo grupoAtual = grupoCadastro.buscarOuFalhar(grupoId);
		grupoInputDisassembler.copyToDomainGrupo(grupoInput, grupoAtual);
		return grupoModelAssembler.toGrupoDTO(grupoCadastro.salvar(grupoAtual)); 		
	}
	
	@DeleteMapping("{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long grupoId) {
		grupoCadastro.excluir(grupoId);
	}

}
