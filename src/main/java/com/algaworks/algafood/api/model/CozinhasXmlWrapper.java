package com.algaworks.algafood.api.model;

import java.util.List;

import com.algaworks.algafood.api.model.output.CozinhaDTO;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;
import lombok.NonNull;

//@JsonRootName(value = "cozinhas")                 // funciona igualmente como o @JacksonXmlRootElement
@JacksonXmlRootElement(localName = "cozinhas")
@Data
public class CozinhasXmlWrapper {
	
//	@JsonProperty(value = "cozinha")                // funciona igualmente como o @JacksonXmlProperty
	@JacksonXmlProperty(localName = "cozinha")
	@NonNull                                        // anotação lombok que força a criacao do construtor com o atributo
	@JacksonXmlElementWrapper(useWrapping = false)  // desabilita a wrapper, que causa repetição do nome da tag 
	private List<CozinhaDTO> cozinhas;

}
