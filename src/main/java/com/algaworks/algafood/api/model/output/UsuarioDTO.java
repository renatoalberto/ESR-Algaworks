package com.algaworks.algafood.api.model.output;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
	
	private Long id;
	private String nome;
	private String email;
	private OffsetDateTime dataCadastro;	

}
