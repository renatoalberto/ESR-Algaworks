package com.algaworks.algafood.api.controller;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.algaworks.algafood.api.assembler.FormaPagamentoInputDisassembler;
import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.model.input.FormaPagamentoInputDTO;
import com.algaworks.algafood.api.model.output.FormaPagamentoDTO;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.FormaPagamentoCadastroService;

@RestController
@RequestMapping("/forma-pagamento")
public class FormaPagamentoController {
	
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@Autowired
	private FormaPagamentoCadastroService formaPagamentoCadastroService; 
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	
	@Autowired
	private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;
	
	@GetMapping
	public ResponseEntity<Collection<FormaPagamentoDTO>> lista(ServletWebRequest request) {
		// 17.9. Implementando requisições condicionais com Deep ETags
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());  // desabilitando o shallowEtagHeaderFilter para criar o Deep Eags
		
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();
		
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		if (request.checkNotModified(eTag)) {
			return null;
		}
		
		Collection<FormaPagamentoDTO> formasPagamento = formaPagamentoModelAssembler.toCollectionDTO(formaPagamentoRepository.findAll());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))   // 17.2. Habilitando o cache com o cabeçalho Cache-Control e a diretiva max-age
				.eTag(eTag)
				.body(formasPagamento);
	}
	
	@GetMapping("/{formaPagamentoId}")
	public ResponseEntity<FormaPagamentoDTO> buscar(ServletWebRequest request, @PathVariable Long formaPagamentoId) {
		// 17.10. Desafio: implementando requisições condicionais com Deep ETags
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());  // desabilitando o shallowEtagHeaderFilter para criar o Deep Eags
		
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.findDataUltimaAtualizacaoById(formaPagamentoId);
		
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		if (request.checkNotModified(eTag)) {
			return null;
		}
		
		FormaPagamentoDTO formaPagamento = formaPagamentoModelAssembler.toFormaPagamentoDTO(formaPagamentoCadastroService.buscarOuFalhar(formaPagamentoId));
		
		return ResponseEntity.ok()
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))                // 17.6. Adicionando outras diretivas de Cache-Control na resposta HTTP
//				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate()) // cachePrivate - para armazenamento apenas no cache local
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())  // cachePublic  - padrão é public
//  		    .cacheControl(CacheControl.noCache())                                   // noCache      - caso exista cache, sempre será solicitado validação no servidor
//  			.cacheControl(CacheControl.noStore())                                   // noStore      - ninguém pode armazenar a rsposta em cache
				.body(formaPagamento);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamento adicionar(@RequestBody @Valid FormaPagamentoInputDTO formaPagamentoInput) {
		FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainFormaPagamento(formaPagamentoInput);
		return formaPagamentoCadastroService.salvar(formaPagamento);
	}
	
	@PutMapping("/{formaPagamentoId}")
	public FormaPagamentoDTO atualizar(@PathVariable Long formaPagamentoId, @RequestBody @Valid FormaPagamentoInputDTO formaPagamentoinput) {
		FormaPagamento formaPagamentoAtual = formaPagamentoCadastroService.buscarOuFalhar(formaPagamentoId);
		
		formaPagamentoInputDisassembler.copyToDomainFormaPagamento(formaPagamentoinput, formaPagamentoAtual);
		
		return formaPagamentoModelAssembler.toFormaPagamentoDTO(formaPagamentoCadastroService.salvar(formaPagamentoAtual));
	}
	
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long formaPagamentoId) {
		formaPagamentoCadastroService.excluir(formaPagamentoId);
	}

}
