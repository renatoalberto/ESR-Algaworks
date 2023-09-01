package com.algaworks.algafood.domain.exception;

public class FormaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaExecption {
	
	private static final String MSG_FORMA_PAGAMENTO_RESTAURANTE_NAO_ENCONTRADA = "Não foi encontrado forma de pagamento com o código %d para o restaurante %d";

	private static final long serialVersionUID = 1L;
	
	private static final String MSG_FORMA_PAGAMENTO_NAO_ENCONTRADA = "Não foi encontrado forma de pagamento com o código %d";
	
	public FormaPagamentoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public FormaPagamentoNaoEncontradaException(Long formaPagamentoId) {
		super(String.format(MSG_FORMA_PAGAMENTO_NAO_ENCONTRADA, formaPagamentoId));
	}
	
	public FormaPagamentoNaoEncontradaException(Long restauranteId, Long formaPagamentoId) {
		super(String.format(MSG_FORMA_PAGAMENTO_RESTAURANTE_NAO_ENCONTRADA, formaPagamentoId, restauranteId));
	}

}
