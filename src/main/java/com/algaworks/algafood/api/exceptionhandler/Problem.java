package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

/**
 * Padronizando o formato de problemas no corpo de respostas com a RFC 7807 - Problem Details for HTTP APIs
 * Especificação do Problem Details for HTTP APIs (RFC 7807) - https://datatracker.ietf.org/doc/html/rfc7807	
 * 
 */
@JsonInclude(Include.NON_NULL)   // Anotação jackson para não retornar propriedade null 
@Builder
@Getter
public class Problem {

	// Padrão da especificação (RFC 7807)
	private Integer status;
	private String type;
	private String title;
	private String detail;
	
	// Especialização
	private String userMessager;
	private OffsetDateTime timestamp;
	private List<Object> objects;
	
	/**
	 * Especialização para tratamento de validação de campos do Bean Validation</br>
	 * Documentação das anotaçãoes Bean Validation - https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-builtin-constraints
	 *
	 */
	@Builder
	@Getter
	public static class Object {
		private String name;
		private String userMessage; 
	}

}
