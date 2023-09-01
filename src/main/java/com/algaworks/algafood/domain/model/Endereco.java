package com.algaworks.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Embeddable  // Essa classe não é uma entidade, mas uma classe com capacidade de ser incorporada, incorporável 
public class Endereco {

	@Column(name="endereco_cep")
	private String cep;
	
	@Column(name="endereco_logradouro")
	private String logradouro;
	
	@Column(name="endereco_numero")
	private String numero;
	
	@Column(name="endereco_complemento")
	private String complemento;
	
	@Column(name="endereco_bairro")
	private String bairro;
                                             // Toda relação ToOne ocorre EagerLoad, gerando uma consulta (separada ou junta), independente do JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)       // Mudamos o padrão para LAZY, buscando a cidade somente quando demandado 
	@JoinColumn(name="endereco_cidade_id")
	private Cidade cidade;

}
