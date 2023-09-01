package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioInputDTO {
	
	@NotBlank
	private String nome;
	
	@Email
	@NotBlank
	private String email;
	
	@Size(min = 6)
	@NotBlank
	private String senha;

}
