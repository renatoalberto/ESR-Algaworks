package com.algaworks.algafood.api.model.output;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoDTO {
	@ApiModelProperty(example = "1")
	private Long id;
	
	@ApiModelProperty(example = "Distrito Federal")
	private String nome;
}
