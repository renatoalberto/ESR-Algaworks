package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.ProdutoInputDisassembler;
import com.algaworks.algafood.api.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.api.model.input.ProdutoInputDTO;
import com.algaworks.algafood.api.model.output.ProdutoDTO;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.ProdutoCadastroService;
import com.algaworks.algafood.domain.service.RestauranteCadastroService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {
	
	@Autowired
	private ProdutoCadastroService produtoCadastroService;
	
	@Autowired
	private RestauranteCadastroService restauranteCadastro;
	
	@Autowired
	private ProdutoModelAssembler produtoModelAssembler;
	
	@Autowired
	private ProdutoInputDisassembler produtoInputDisassembler;
	
	@GetMapping
	public List<ProdutoDTO> listar(@PathVariable Long restauranteId, @RequestParam(required = false) boolean incluirInativos ) {
		List<Produto> produtos;
		
		if (incluirInativos) {
			produtos = produtoCadastroService.buscarTodosProdutosRestaurante(restauranteId);
		} else {
			produtos = produtoCadastroService.buscarProdutosAtivosRestaurante(restauranteId);
		}
		
		return produtoModelAssembler.toCollectionProdutoDTO(produtos);
	}
	
	@GetMapping("/{produtoId}")
	public ProdutoDTO listar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		Produto produto = produtoCadastroService.buscarProdutoRestaurante(restauranteId, produtoId);
		
		return produtoModelAssembler.toProdutoDTO(produto);
	}
	
	@PostMapping
	public ProdutoDTO adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInputDTO produtoInputDTO) {
		Produto produto = produtoInputDisassembler.toDomainProduto(produtoInputDTO);
		
		Restaurante restaurante = restauranteCadastro.buscarOuFalhar(restauranteId);
		produto.setRestaurante(restaurante);
		
		return produtoModelAssembler.toProdutoDTO(produtoCadastroService.salvar(produto));
	}
	
	@PutMapping("{produtoId}")
	public ProdutoDTO atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestBody @Valid ProdutoInputDTO produtoInputDTO) {
		Produto ProdutoAtual = produtoCadastroService.buscarProdutoRestaurante(restauranteId, produtoId);
		
		produtoInputDisassembler.copyToDomainProduto(produtoInputDTO, ProdutoAtual);
		
		return produtoModelAssembler.toProdutoDTO(produtoCadastroService.salvar(ProdutoAtual));
	}
	
}
