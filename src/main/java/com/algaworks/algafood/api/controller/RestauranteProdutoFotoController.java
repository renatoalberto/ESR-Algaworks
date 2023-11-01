package com.algaworks.algafood.api.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.api.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.model.input.FotoProdutoInputDTO;
import com.algaworks.algafood.api.model.output.FotoProdutoDTO;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.FotoProdutoService;
import com.algaworks.algafood.domain.service.ProdutoCadastroService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {
	
	@Autowired
	private FotoProdutoService fotoProdutoService;
	
	@Autowired
	private ProdutoCadastroService produtoCadastro;
	
	@Autowired
	private FotoProdutoModelAssembler fotoProdutoAssembler;

	@GetMapping
	public FotoProdutoDTO buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		FotoProduto fotoProduto = fotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
		
		return fotoProdutoAssembler.toFotoProdutoDTO(fotoProduto);
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
	
}
