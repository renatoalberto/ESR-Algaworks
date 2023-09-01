package com.algaworks.algafood.api.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.model.output.FormaPagamentoDTO;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.FormaPagamentoCadastroService;
import com.algaworks.algafood.domain.service.RestauranteCadastroService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController {
	
	@Autowired
	private RestauranteCadastroService restauranteCadastro;
	
	@Autowired
	private FormaPagamentoCadastroService formaPagamentoCadastroService;
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	
	@GetMapping
	public Collection<FormaPagamentoDTO> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante =  restauranteCadastro.buscarOuFalhar(restauranteId);
		Collection<FormaPagamento> formasPagamento = restaurante.getFormasPagamento();
		
		return formaPagamentoModelAssembler.toCollectionDTO(formasPagamento); 
	}
	
	@DeleteMapping("{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		Restaurante restaurante =  restauranteCadastro.buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = formaPagamentoCadastroService.buscarOuFalhar(formaPagamentoId);
		
		restauranteCadastro.desassociarFormaPagamento(restaurante, formaPagamento);
	}
	
	@PutMapping("{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void adicionar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		Restaurante restaurante =  restauranteCadastro.buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = formaPagamentoCadastroService.buscarOuFalhar(formaPagamentoId);
		
		restauranteCadastro.associarFormaPagamento(restaurante, formaPagamento);
	}

}
