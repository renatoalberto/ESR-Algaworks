package com.algaworks.algafood.domain.exception;

public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaExecption {
	
	private static final long serialVersionUID = 1L;
	
	private static final String MSG_CIDADE_NAO_ENCONTRADA = "Não existe cidade cadastrado para o código %d";

	public CidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public CidadeNaoEncontradaException(Long cidadeId) {
		super(String.format(MSG_CIDADE_NAO_ENCONTRADA, cidadeId));
	}

}
