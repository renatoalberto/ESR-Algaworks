package com.algaworks.algafood.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaExecption {

	private static final String MSG_USUARIO_RESTAURANTE_NAO_ENCONTRADO = "Não existe usuário com o código %d para o restaurante com o código %d";

	private static final long serialVersionUID = 1L;
	
	private static final String MSG_USUARIO_NAO_ENCONTRADO = "Não existe usuário com o código %d";

	public UsuarioNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public UsuarioNaoEncontradoException(Long usuarioId) {
		super(String.format(MSG_USUARIO_NAO_ENCONTRADO, usuarioId));
	}
	
	public UsuarioNaoEncontradoException(Long restauranteId, Long usuarioId) {
		super(String.format(MSG_USUARIO_RESTAURANTE_NAO_ENCONTRADO, usuarioId, restauranteId));
	}

}
