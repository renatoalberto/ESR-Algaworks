package com.algaworks.algafood.domain.exception;

public class FotoProdutoNaoEncontradaExecption extends EntidadeNaoEncontradaExecption {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG_FOTO_PRODUTO_NAO_ENCONTRADA = "Não foi encontrado foto do produto %d para o restaurante %d";
	
	public FotoProdutoNaoEncontradaExecption(Long restauranteId, Long produtoId) {
		super(String.format(MSG_FOTO_PRODUTO_NAO_ENCONTRADA, produtoId, restauranteId));
	}

}
