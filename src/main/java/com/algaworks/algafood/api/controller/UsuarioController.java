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

import com.algaworks.algafood.api.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.input.UsuarioInputDTO;
import com.algaworks.algafood.api.model.input.UsuarioSenhaInputDTO;
import com.algaworks.algafood.api.model.output.UsuarioDTO;
import com.algaworks.algafood.domain.exception.UsuarioSenhaInvalidaException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.UsuarioCadastroService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioCadastroService usuarioCadastro;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@Autowired
	private UsuarioInputDisassembler usuarioInputDisassembler;
	
	@GetMapping
	public List<UsuarioDTO> listar() {
		System.out.println("Entrou aqui");
		return usuarioModelAssembler.toCollectionDTO(usuarioRepository.findAll());
	}
	
	@GetMapping("/{usuarioId}")
	public UsuarioDTO buscar(@PathVariable Long usuarioId) {
		return usuarioModelAssembler.toUsuarioDTO(usuarioCadastro.buscarOuFalhar(usuarioId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioDTO adicionar(@RequestBody @Valid UsuarioInputDTO usuarioInput) {
		Usuario usuario = usuarioInputDisassembler.toDomainUsuario(usuarioInput);
		return usuarioModelAssembler.toUsuarioDTO(usuarioCadastro.salvar(usuario));
	}
	
	@PutMapping("/{usuarioId}")
	public UsuarioDTO atualizar(@PathVariable Long usuarioId, @RequestBody @Valid UsuarioInputDTO usuarioInput) {
		Usuario usuario = usuarioCadastro.buscarOuFalhar(usuarioId);

		if (usuario.getSenha().equals(usuarioInput.getSenha())) {
			usuarioInputDisassembler.copyToDomainUsuario(usuarioInput, usuario);
			return usuarioModelAssembler.toUsuarioDTO(usuarioCadastro.salvar(usuario));
		}
		
		throw new UsuarioSenhaInvalidaException();
	}
	
	@PutMapping("/{usuarioId}/senha")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarSenha(@PathVariable Long usuarioId, @RequestBody @Valid UsuarioSenhaInputDTO usuarioInput) {
		Usuario usuario = usuarioCadastro.buscarOuFalhar(usuarioId);
		
		if (usuario.getSenha().equals(usuarioInput.getSenhaAtual())) {
			usuario.setSenha(usuarioInput.getNovaSenha());
			usuarioModelAssembler.toUsuarioDTO(usuarioCadastro.salvar(usuario));
		} else {
			throw new UsuarioSenhaInvalidaException();			
		}
		
	}
	
	@DeleteMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long usuarioId) {
		usuarioCadastro.excluir(usuarioId);
	}

}
