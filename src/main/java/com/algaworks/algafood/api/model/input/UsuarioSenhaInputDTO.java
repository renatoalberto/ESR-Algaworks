package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioSenhaInputDTO {
	
	@NotBlank
	@Size(min = 6)
	private String senhaAtual;
	
	@NotBlank
	@Size(min = 6)
	private String novaSenha;

}
