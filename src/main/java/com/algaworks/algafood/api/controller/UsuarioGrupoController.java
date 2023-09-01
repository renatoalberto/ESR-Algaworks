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

import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.model.output.GrupoDTO;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.GrupoCadastroService;
import com.algaworks.algafood.domain.service.UsuarioCadastroService;

@RestController
@RequestMapping("/usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController {
	
	@Autowired
	private UsuarioCadastroService usuarioCadastro;
	
	@Autowired
	private GrupoCadastroService grupoCadastro;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@GetMapping
	public Collection<GrupoDTO> listar(@PathVariable Long usuarioId) {
		Usuario usuario = usuarioCadastro.buscarOuFalhar(usuarioId);
		
		Set<Grupo> grupos = usuario.getGrupos();
		
		return grupoModelAssembler.collectionToDTO(grupos);
	}
	
	@GetMapping("/{grupoId}")
	public GrupoDTO buscar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		Usuario usuario = usuarioCadastro.buscarOuFalhar(usuarioId);
		Grupo grupo = grupoCadastro.buscarOuFalhar(grupoId);
		
		if (!usuario.getGrupos().contains(grupo)) {
			throw new GrupoNaoEncontradoException(usuarioId, grupoId);
		}
		
		return grupoModelAssembler.toGrupoDTO(grupo);
	}
	
	@PutMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void adicionar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		Usuario usuario = usuarioCadastro.buscarOuFalhar(usuarioId);
		Grupo grupo = grupoCadastro.buscarOuFalhar(grupoId);
		
		usuario.adicionarGrupo(grupo);
		
		usuarioCadastro.salvar(usuario);
	}
	
	@DeleteMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		Usuario usuario = usuarioCadastro.buscarOuFalhar(usuarioId);
		Grupo grupo = grupoCadastro.buscarOuFalhar(grupoId);
		
		usuario.removerGrupo(grupo);
		
		usuarioCadastro.salvar(usuario);
	}
	
	

}
