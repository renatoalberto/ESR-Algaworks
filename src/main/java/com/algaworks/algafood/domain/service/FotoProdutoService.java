package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradaExecption;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.FotoStorageService;
import com.algaworks.algafood.domain.model.FotoStorageService.NovaFoto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class FotoProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private FotoStorageService fotoStorageService;
	
	@Transactional
	public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo) {
		Long restauranteId = foto.getProduto().getRestaurante().getId();
		Long produtoId = foto.getProduto().getId();
		String nomeArquivoNovo = fotoStorageService.geraNomeArquivo(foto.getNomeArquivo());
		String nomeArquivoAntigo = null;
		
		Optional<FotoProduto> fotoAntiga = produtoRepository.findFotoById(restauranteId, produtoId);
		
		if (fotoAntiga.isPresent()) {
			produtoRepository.delete(fotoAntiga.get());
			nomeArquivoAntigo = fotoAntiga.get().getNomeArquivo();
		}
		
		foto.setNomeArquivo(nomeArquivoNovo);
		foto = produtoRepository.savarFoto(foto);
		produtoRepository.flush();
		
		NovaFoto novaFoto = NovaFoto.builder()
				.nomeArquivo(foto.getNomeArquivo())
				.inputStream(dadosArquivo)
				.build();
		
		fotoStorageService.substituir(nomeArquivoAntigo, novaFoto);
		
		return foto;
	}
	
	public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return produtoRepository.findFotoById(restauranteId, produtoId).orElseThrow(() -> new FotoProdutoNaoEncontradaExecption(restauranteId, produtoId));
	}

}
