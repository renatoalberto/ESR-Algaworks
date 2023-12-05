package com.algaworks.algafood.api.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.api.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.model.input.FotoProdutoInputDTO;
import com.algaworks.algafood.api.model.output.FotoProdutoDTO;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaExecption;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.FotoStorageService;
import com.algaworks.algafood.domain.model.FotoStorageService.FotoRecuperada;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.FotoProdutoService;
import com.algaworks.algafood.domain.service.ProdutoCadastroService;
import com.google.common.net.HttpHeaders;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {
	
	@Autowired
	private FotoProdutoService fotoProdutoService;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoCadastroService produtoCadastro;
	
	@Autowired
	private FotoProdutoModelAssembler fotoProdutoAssembler;
	
	@Autowired
	private FotoStorageService fotoStorage;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public FotoProdutoDTO buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		FotoProduto fotoProduto = fotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
		
		return fotoProdutoAssembler.toFotoProdutoDTO(fotoProduto);
	}
	
//	@GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
	@GetMapping
	public ResponseEntity<?> servirFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,@RequestHeader(name="accept") String acceptHeader)  throws HttpMediaTypeNotAcceptableException {
		try {
			FotoProduto fotoProduto = fotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
			
			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
			List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);
			
			verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);
			
			FotoRecuperada fotoRecuperada = fotoStorage.recupera(fotoProduto.getNomeArquivo());
			
			if(fotoRecuperada.temInputStream()) {
				return ResponseEntity.ok()
						.contentType(mediaTypeFoto)
						.body(new InputStreamResource(fotoRecuperada.getInputStream()));							
			}
			
			return ResponseEntity.status(HttpStatus.FOUND)
					.header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
					.build();
		} catch (EntidadeNaoEncontradaExecption e) {
			return ResponseEntity.notFound().build();
		}
	}
		
	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
		boolean compativel = mediaTypesAceitas.stream()
				.anyMatch(mediaTypesAceita -> mediaTypesAceita.isCompatibleWith(mediaTypeFoto));   // isCompatibleWith permite image/*
		
		if (!compativel) {
			System.out.println("Não é compativel");
			throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
		}
	}

	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoDTO atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid FotoProdutoInputDTO fotoProdutoInput) throws IOException {
		MultipartFile arquivo = fotoProdutoInput.getArquivo();
		
//		var nomeArquivo = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
		
//		var enderecoArquivoFoto = Path.of("./src/main/resources/foto-upload/" + nomeArquivo);
//		var enderecoArquivoFoto = Path.of(".\\src\\main\\resources\\foto-upload\\" + nomeArquivo);
//		var enderecoArquivoFoto = Path.of("C:\\Users\\renat\\Desktop\\Algaworks\\Especialista Spring Rest\\workspace_especialista_spring_rest_esr\\algafood-api\\src\\main\\resources\\foto-upload\\" + nomeArquivo);
		
		System.out.println(arquivo.getContentType());
		System.out.println(fotoProdutoInput.getDescricao());
//		System.out.println(enderecoArquivoFoto);
		
//		try {
//			fotoProdutoInput.getArquivo().transferTo(enderecoArquivoFoto);			
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
		
		// Persistindo a foto no banco de dados
		Produto produto = produtoCadastro.buscarProdutoRestaurante(restauranteId, produtoId);
		
		FotoProduto foto = new FotoProduto();
		foto.setNomeArquivo(arquivo.getOriginalFilename());
		foto.setDescricao(fotoProdutoInput.getDescricao());
		foto.setContentType(arquivo.getContentType());
		foto.setTamanho(arquivo.getSize());
		foto.setProduto(produto);

		FotoProduto fotoSalva = fotoProdutoService.salvar(foto, arquivo.getInputStream());
				
		return fotoProdutoAssembler.toDTO(fotoSalva);
		
	}
	
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		FotoProduto fotoProduto = fotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
		
		produtoRepository.delete(fotoProduto);
		produtoRepository.flush();
		
		fotoStorage.removerFotoAntiga(fotoProduto.getNomeArquivo());
	}
	
	
}
