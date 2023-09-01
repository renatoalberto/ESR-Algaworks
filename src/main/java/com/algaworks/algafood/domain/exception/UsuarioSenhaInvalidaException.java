package com.algaworks.algafood.domain.exception;

public class UsuarioSenhaInvalidaException extends NegocioExecption {

	private static final long serialVersionUID = 1L;
	
	public UsuarioSenhaInvalidaException() {
		super("Senha atual informada não coincide com a senha do usuário");
	}

}
