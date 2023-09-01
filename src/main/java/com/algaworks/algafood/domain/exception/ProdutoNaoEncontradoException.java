package com.algaworks.algafood.domain.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaExecption {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG_PRODUTO_NAO_ENCONTRADO = "Não foi possível localiza o produto com o código %d";
	
	private static final String MSG_PRODUTO_DE_RESTAURANTE_NAO_ENCONTRADO = "Não foi possível localiza o produto com o código %d para o restaurante com o código %d";

	public ProdutoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public ProdutoNaoEncontradoException(Long produtoId) {
		super(String.format(MSG_PRODUTO_NAO_ENCONTRADO, produtoId));
	}
	
	public ProdutoNaoEncontradoException(Long restauranteId, Long produtoId) {
		super(String.format(MSG_PRODUTO_DE_RESTAURANTE_NAO_ENCONTRADO, produtoId, restauranteId));
	}
	

}
