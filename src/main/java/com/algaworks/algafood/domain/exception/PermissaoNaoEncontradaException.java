package com.algaworks.algafood.domain.exception;

public class PermissaoNaoEncontradaException extends EntidadeNaoEncontradaExecption {

	private static final String MSG_PERMISSAO_GRUPO_NAO_ENCONTRADA = "Não foi encontrada permissao com o código %d para o grupo %d";

	private static final long serialVersionUID = 1L;
	
	private static final String MSG_PERMISSAO_NAO_ENCONTRADA = "Não foi encontrada permissao para o código %d";

	public PermissaoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public PermissaoNaoEncontradaException(Long permissaoId) {
		super(String.format(MSG_PERMISSAO_NAO_ENCONTRADA, permissaoId));
	}
	
	public PermissaoNaoEncontradaException(Long grupoId, Long permissaoId) {
		super(String.format(MSG_PERMISSAO_GRUPO_NAO_ENCONTRADA, permissaoId, grupoId));
	}

}
