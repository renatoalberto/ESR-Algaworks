package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaExecption {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG_PEDIDO_NAO_ENCONTRADO = "Nao foi encontrado pedido com o código %d";

	public PedidoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public PedidoNaoEncontradoException(Long pedidoId) {
		super(String.format(MSG_PEDIDO_NAO_ENCONTRADO, pedidoId));
	}

}
