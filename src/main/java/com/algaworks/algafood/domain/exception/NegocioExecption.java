package com.algaworks.algafood.domain.exception;

public class NegocioExecption extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NegocioExecption(String mensagem) {
		super(mensagem);
	}
	
	public NegocioExecption(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

}
