package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * Padronizando o formato de problemas no corpo de respostas com a RFC 7807 - Problem Details for HTTP APIs
 * Especificação do Problem Details for HTTP APIs (RFC 7807) - https://datatracker.ietf.org/doc/html/rfc7807	
 * 
 */
@ApiModel("Problema") // Documentação swagger
@JsonInclude(Include.NON_NULL)   // Anotação jackson para não retornar propriedade null 
@Builder
@Getter
public class Problem {

	// Padrão da especificação (RFC 7807)
	@ApiModelProperty(example = "400", position = 5)
	private Integer status;
	
	@ApiModelProperty(example = "https://www.algafood.com.br/dados-invalidos", position = 10)
	private String type;
	
	@ApiModelProperty(example = "Dados inválidos", position = 15)
	private String title;
	
	@ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.", position = 20)
	private String detail;
	
	// Especialização
	@ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.", position = 25)
	private String userMessager;
	
	@ApiModelProperty(example = "2024-02-13T11:35.70844Z", value="Data e hora no formato ISO", position = 30)
	private OffsetDateTime timestamp;
	
	@ApiModelProperty(value = "Lista de objetos ou campos que geraram o erro (opcional)", position = 35)
	private List<Object> objects;
	
	/**
	 * Especialização para tratamento de validação de campos do Bean Validation</br>
	 * Documentação das anotaçãoes Bean Validation - https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-builtin-constraints
	 *
	 */
	@ApiModel(value = "ObjetoProblema") // Documentação swagger
	@Builder
	@Getter
	public static class Object {
		
		@ApiModelProperty(example = "preço")
		private String name;
		
		@ApiModelProperty(example = "O preço é obrigatório.")
		private String userMessage; 
	}

}
