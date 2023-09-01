package com.algaworks.algafood.domain.exception;

public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaExecption {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG_GRUPO_USUARIO_NAO_ENCONTRADO = "Não foi encontrado grupo com id %d para o usuario com código %d";
	private static final String MSG_GRUPO_NAO_ENCONTRADO = "Não foi encontrado grupo com id %d";

	public GrupoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public GrupoNaoEncontradoException(Long grupoId) {
		super(String.format(MSG_GRUPO_NAO_ENCONTRADO, grupoId));
	}
	
	public GrupoNaoEncontradoException(Long usuarioId, Long grupoId) {
		super(String.format(MSG_GRUPO_USUARIO_NAO_ENCONTRADO, grupoId, usuarioId));
	}

}
