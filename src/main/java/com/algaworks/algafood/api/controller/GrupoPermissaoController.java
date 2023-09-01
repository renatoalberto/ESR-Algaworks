package com.algaworks.algafood.api.controller;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.api.model.output.PermissaoDTO;
import com.algaworks.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.GrupoCadastroService;
import com.algaworks.algafood.domain.service.PermissaoCadastroService;

@RestController
@RequestMapping("/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {
	
	@Autowired	
	private GrupoCadastroService grupoCadastro;
	
	@Autowired
	private PermissaoCadastroService permissaoCadastro;
	
	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler; 
	
	@GetMapping 
	public Collection<PermissaoDTO> listar(@PathVariable Long grupoId) {
		Grupo grupo = grupoCadastro.buscarOuFalhar(grupoId);
		Set<Permissao> permissoes = grupo.getPermissoes();
		return permissaoModelAssembler.toCollectionPermissaoDTO(permissoes); 
	}
	
	@GetMapping("/{permissaoId}")
	public PermissaoDTO listar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		Grupo grupo = grupoCadastro.buscarOuFalhar(grupoId);
		
		Permissao permissao = permissaoCadastro.buscarOuFalhar(permissaoId);
		
		if (!grupo.getPermissoes().contains(permissao)) {
			throw new PermissaoNaoEncontradaException(grupoId, permissaoId);
		}
		
		return permissaoModelAssembler.toPermissaoDTO(permissao); 
	}
	
	@PutMapping("{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void adicionar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		Grupo grupo = grupoCadastro.buscarOuFalhar(grupoId);
		
		Permissao permissao = permissaoCadastro.buscarOuFalhar(permissaoId);
		
		grupo.getPermissoes().add(permissao);
		
		grupoCadastro.salvar(grupo);
	}
	
	@DeleteMapping("{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		Grupo grupo = grupoCadastro.buscarOuFalhar(grupoId);
		
		Permissao permissao = permissaoCadastro.buscarOuFalhar(permissaoId);
		
		grupo.getPermissoes().remove(permissao);
		
		grupoCadastro.salvar(grupo);
	}

}
