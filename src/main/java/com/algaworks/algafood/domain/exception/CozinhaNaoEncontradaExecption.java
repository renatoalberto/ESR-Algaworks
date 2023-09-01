package com.algaworks.algafood.domain.exception;

public class CozinhaNaoEncontradaExecption extends EntidadeNaoEncontradaExecption {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG_COZINHA_NAO_ENCONTRADA = "Não foi encontrada cozinha com código %d";

	public CozinhaNaoEncontradaExecption(String mensagem) {
		super(mensagem);
	}
	
	public CozinhaNaoEncontradaExecption(Long cozinhaId) {
		super(String.format(MSG_COZINHA_NAO_ENCONTRADA, cozinhaId));
	}

}
