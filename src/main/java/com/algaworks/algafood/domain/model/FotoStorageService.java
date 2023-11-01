package com.algaworks.algafood.domain.model;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

public interface FotoStorageService {

	InputStream recupera(String nomeArquivo);
	
	void armazenar(NovaFoto novaFoto);
	
	void removerFotoAntiga(String nomeArquivo);
	
	default void substituir(String nomeFotoAntiga, NovaFoto novafoto) {
		this.armazenar(novafoto);
		
		if (nomeFotoAntiga != null) {
			this.removerFotoAntiga(nomeFotoAntiga);
		}
	}
	
	default String geraNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString() + "_" + nomeOriginal;
	}
	
	@Builder
	@Getter
	class NovaFoto {
		private String nomeArquivo;
		private InputStream inputStream;
	}
	
	
}
