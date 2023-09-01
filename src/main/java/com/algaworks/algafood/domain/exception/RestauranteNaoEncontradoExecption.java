package com.algaworks.algafood.domain.exception;

public class RestauranteNaoEncontradoExecption extends EntidadeNaoEncontradaExecption {
	
	private static final long serialVersionUID = 1L;
	
	private static final String MSG_RESTAURANTE_NAO_ENCONTRADA = "Não existe cadastro de restaurante com o código %d";

	public RestauranteNaoEncontradoExecption(String mensagem) {
		super(mensagem);
	}
	
	public RestauranteNaoEncontradoExecption(Long restauranteId) {
		super(String.format(MSG_RESTAURANTE_NAO_ENCONTRADA, restauranteId));
	}

}
