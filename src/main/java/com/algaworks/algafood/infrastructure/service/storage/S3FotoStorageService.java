package com.algaworks.algafood.infrastructure.service.storage;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;

import com.algaworks.algafood.core.storge.StoragePropeties;
import com.algaworks.algafood.domain.model.FotoStorageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

//@Service
public class S3FotoStorageService implements FotoStorageService {
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private StoragePropeties storagePropeties;

	@Override
	public FotoRecuperada recupera(String nomeArquivo) {
		String caminhoArquivo = getCaminhoArquivo(nomeArquivo);
		
		URL url =  amazonS3.getUrl(storagePropeties.getS3().getBucket(), caminhoArquivo);
		
		FotoRecuperada fotoRecuperada = FotoRecuperada.builder()
				.url(url.toString())
				.build();
		
		return fotoRecuperada;
	}

	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			String caminhoArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());
			
			var objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(novaFoto.getContentType());
			
			var putObjectRequest = new PutObjectRequest(
					storagePropeties.getS3().getBucket(),
					caminhoArquivo, 
					novaFoto.getInputStream(),
					objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead);  // permite que foto seja acessado publicamente
			
			amazonS3.putObject(putObjectRequest);			
		} catch (Exception e) {
			throw new StorageException("Não foi possível enviar arquivo para Amazon S3.", e);
		}
	}

	private String getCaminhoArquivo(String nomeArquivo) {
		return String.format("%s/%s", storagePropeties.getS3().getDiretorioFotos(), nomeArquivo);
	}

	@Override
	public void removerFotoAntiga(String nomeArquivo) {
		try {		
			String caminhoArquivo = getCaminhoArquivo(nomeArquivo);
			
			var deleteObjectRequest = new DeleteObjectRequest(
					storagePropeties.getS3().getBucket(),
					caminhoArquivo);
			
			amazonS3.deleteObject(deleteObjectRequest);	
		} catch (Exception e) {
			throw new StorageException("Não foi possível enviar arquivo para Amazon S3.", e);
		}
	}

	
	
}
