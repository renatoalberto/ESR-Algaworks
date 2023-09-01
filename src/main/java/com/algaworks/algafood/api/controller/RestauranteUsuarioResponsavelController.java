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

import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.output.UsuarioDTO;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.RestauranteCadastroService;
import com.algaworks.algafood.domain.service.UsuarioCadastroService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController {
	
	@Autowired
	private RestauranteCadastroService restauranteCadastro;
	
	@Autowired
	private UsuarioCadastroService usuarioCadastro;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@GetMapping
	public Collection<UsuarioDTO> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante = restauranteCadastro.buscarOuFalhar(restauranteId);
		
		Set<Usuario> responsaveis = restaurante.getResponsaveis();
		
		return usuarioModelAssembler.toCollectionDTO(responsaveis);
	}
	
	@GetMapping("/{usuarioId}")
	public UsuarioDTO listar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		Restaurante restaurante = restauranteCadastro.buscarOuFalhar(restauranteId);
		Usuario responsavel = usuarioCadastro.buscarOuFalhar(usuarioId);
		
		Set<Usuario> responsaveis = restaurante.getResponsaveis();
		
		if (!responsaveis.contains(responsavel)) {
			throw new UsuarioNaoEncontradoException(restauranteId, usuarioId);
		}
		
		return usuarioModelAssembler.toUsuarioDTO(responsavel);
	}
	
	@PutMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteCadastro.associarResponsavel(restauranteId, usuarioId);
	}
	
	@DeleteMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteCadastro.desassociarResponsavel(restauranteId, usuarioId);
	}

}
