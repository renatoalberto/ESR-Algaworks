package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.input.CidadeInputDTO;
import com.algaworks.algafood.api.model.output.CidadeDTO;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaExecption;
import com.algaworks.algafood.domain.exception.NegocioExecption;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CidadeCadastroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Api(tags = "Cidades")
@RestController
@RequestMapping("/cidades") 
public class CidadeController {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CidadeCadastroService cidadeCadastro;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;
	
	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;
	
	@ApiOperation("Listar as cidades")
	@GetMapping
	public List<CidadeDTO> listar() {
		return cidadeModelAssembler.toCollectionDTO(cidadeRepository.findAll());
	}
	
	@ApiOperation("Busca uma cidade por id")
	@ApiResponses({                                                    // 18.16. Swagger -  Descrevendo códigos de status de respostas em endpoints específicos
		@ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class))), 
		@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
	})
	@GetMapping("/{cidadeId}")
	public CidadeDTO buscar(@ApiParam(name="cidadeId", value = "Id de uma cidade", example = "1") @PathVariable Long cidadeId) {  //Documentação Swagger @ApiParam name="cidadeId" mesmo no para exemplo, não obrigatório
		return cidadeModelAssembler.toDTO(cidadeCadastro.buscarOuFalhar(cidadeId));
	}
	
//	@ApiOperation("Inclui uma cidade")
//	@PostMapping
//	public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
//		try {
//			cidade = cidadeCadastro.salvar(cidade);
//			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
//		} catch (EstadoNaoEncontradaExecption e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//	}
	
	@ApiOperation("Cadastro de uma cidade")
	@ApiResponses({                                                    // 18.16. Swagger - Descrevendo códigos de status de respostas em endpoints específicos
		@ApiResponse(responseCode = "201", description = "Cidade cadastrada")
	})	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeDTO adicionar(@ApiParam(name = "corpo", value="Representação de uma nova cidade") @RequestBody @Valid CidadeInputDTO cidadeInput) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainCidade(cidadeInput);
			return cidadeModelAssembler.toDTO(cidadeCadastro.salvar(cidade));
		} catch (EstadoNaoEncontradaExecption e) {
			throw new NegocioExecption(e.getMessage(), e);
		}
	}
	
	
	@ApiOperation("Atualiza uma cidade por id")
	@ApiResponses({                                                    // 18.16. Swagger -  Descrevendo códigos de status de respostas em endpoints específicos
		@ApiResponse(responseCode = "200", description = "Cidade atualizada"), 
		@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
	})	
	@PutMapping("/{cidadeId}")
	public CidadeDTO atualizar(@ApiParam(name="cidadeId", value = "Id de uma cidade", example = "1") @PathVariable long cidadeId, @ApiParam(name = "corpo", value="Representação de uma nova cidade com novos dados") @RequestBody @Valid CidadeInputDTO cidadeInput) {
		Cidade cidadeAtual = cidadeCadastro.buscarOuFalhar(cidadeId);
		
//		BeanUtils.copyProperties(cidade, cidadeAtual, "id");
		cidadeInputDisassembler.copyToDomainCidade(cidadeInput, cidadeAtual);
		
		try {
			return cidadeModelAssembler.toDTO(cidadeCadastro.salvar(cidadeAtual));			
		} catch (EstadoNaoEncontradaExecption e) {
			throw new NegocioExecption(e.getMessage(), e);
		}
	}
	
	@ApiOperation("Exclui uma cidade por id")
	@ApiResponses({                                                    // 18.16. Swagger -  Descrevendo códigos de status de respostas em endpoints específicos
		@ApiResponse(responseCode = "204", description = "Cidade excluida"), 
		@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Problem.class)))
	})	
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@ApiParam(name="cidadeId", value = "Id da cidade que será excluida", example = "1") @PathVariable Long cidadeId) {
		cidadeCadastro.excluir(cidadeId);
	}

}
