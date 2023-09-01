package com.algaworks.algafood.api.model.mixin;

import java.util.ArrayList;
import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class CozinhaMixin {
	
	@JsonProperty(value = "nomeDaCozinha")            // anotação jackson altera o nome do aatributo na resposta
	private String nome;
		
	@JsonIgnore                                       // anotação jackson para omitir o atributo na resposta
	private List<Restaurante> restaurantes = new ArrayList<Restaurante>();

}
