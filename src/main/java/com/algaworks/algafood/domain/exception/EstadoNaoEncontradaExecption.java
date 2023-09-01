package com.algaworks.algafood.domain.exception;

public class EstadoNaoEncontradaExecption extends EntidadeNaoEncontradaExecption {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG_ESTADO_NAO_ENCONTRADA = "Não foi encontrado estado com o código %d";
	
	public EstadoNaoEncontradaExecption(String mensagem) {
		super(mensagem);
	}
	
	public EstadoNaoEncontradaExecption(Long estadoId) {
		super(String.format(MSG_ESTADO_NAO_ENCONTRADA, estadoId));
	}

}
