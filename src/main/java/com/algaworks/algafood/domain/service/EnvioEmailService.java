package com.algaworks.algafood.domain.service;

import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

public interface EnvioEmailService {
	
	void enviar(Mensagem mensagem);
	
	@Getter
	@Builder
	class Mensagem {
		
		@Singular   // do lombok, permite criar um item sem instanciar o Set
		private Set<String> destinatarios;  // Set para impedir que exista email duplicado
		
		@NonNull    // do lombok, valida se não carregado retorna exception
		private String assunto;
		
		@NonNull
		private String corpo;
		
		@Singular("variavel")                   // Singular não conseguiu resolver a palavra variaveis, portanto foi informado
		private Map<String, Object> variaveis; 
	}

}
