package com.algaworks.algafood.infrastructure.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.domain.model.FotoStorageService;

@Service
public class LocalFotoStorageService implements FotoStorageService {
	
	@Value("${algafood.storage.local.diretorio-fotos}")
	private Path diretorioFotos;

	@Override
	public void armazenar(NovaFoto novaFoto) {
		
		try {
			Path pathArquivoFoto = getArquivoPath(novaFoto.getNomeArquivo());
			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(pathArquivoFoto));
		} catch (IOException e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}
		
	}
	
	@Override
	public void removerFotoAntiga(String nomeArquivo) {
		Path pathArquivoAntigo = getArquivoPath(nomeArquivo);
		
		try {
			Files.deleteIfExists(pathArquivoAntigo);
		} catch (IOException e) {
			throw new StorageException("Não foi possível remover arquivo antigo.", e);
		}
		
	}
	
	@Override
	public InputStream recupera(String nomeArquivo) {
		Path pathArquivoAntigo = getArquivoPath(nomeArquivo);
		
		try {
			return Files.newInputStream(pathArquivoAntigo);
		} catch (IOException e) {
			throw new StorageException("Não foi possível recuperar arquivo.", e);
		}
	}
	
	private Path getArquivoPath(String nomeArquivo) {
		return diretorioFotos.resolve(Path.of(nomeArquivo));
	}


}
