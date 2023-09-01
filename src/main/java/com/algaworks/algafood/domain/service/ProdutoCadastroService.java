package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class ProdutoCadastroService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private RestauranteCadastroService restauranteCadastro;
	
	public Produto buscarOuFalhar(Long produtoId) {
		return produtoRepository.findById(produtoId).orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));
	}
	
	public Produto buscarProdutoRestaurante(Long restauranteId, Long produtoId) {
		Restaurante restaurante = restauranteCadastro.buscarOuFalhar(restauranteId);
		
		Produto produto = buscarOuFalhar(produtoId);
		
		if (!produto.getRestaurante().equals(restaurante)) {
			throw new ProdutoNaoEncontradoException(restauranteId, produtoId);
		}
		
		return produto;
	}
	
	public List<Produto> buscarProdutosRestaurante(Long restauranteId) {
		Restaurante restaurante = restauranteCadastro.buscarOuFalhar(restauranteId);	
		return produtoRepository.findByRestaurante(restaurante);
	}
	
	@Transactional
	public Produto salvar(Produto produto) {
		return produtoRepository.save(produto);
	}

}
