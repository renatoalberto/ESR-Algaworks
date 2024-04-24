package com.algaworks.algafood.api.model.output;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="Cidade (CidadeDTO)", description = "Representa uma cidade")  // documentação swagger
@Getter
@Setter
public class CidadeDTO {
	
	@ApiModelProperty(value="ID da cidade", example="1")
	private Long id;
	
	@ApiModelProperty(example="Brasília", required = true)
	@NotBlank
	private String nome;
	
	@Valid
	@NotNull
	private EstadoDTO estado;

}
