package com.algaworks.algafood.domain.exception;

//public class EntidadeNaoEncontradaExecption extends ResponseStatusException {
//@ResponseStatus(value = HttpStatus.NOT_FOUND) // , reason = "Entidade não encontrada")
public abstract class EntidadeNaoEncontradaExecption extends NegocioExecption {

	private static final long serialVersionUID = 1L;
	
//	public EntidadeNaoEncontradaExecption(HttpStatus status, String mensagem) {
//		super(status, mensagem);
//	}
	
	public EntidadeNaoEncontradaExecption(String mensagem) {
//		this(HttpStatus.NOT_FOUND, mensagem);
		super(mensagem);
	}

}
