package com.algaworks.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table
public class FotoProduto {
	
	@EqualsAndHashCode.Include
	@Id
	@Column(name = "produto_id")
	private Long id;
	
	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;
	
	@OneToOne(fetch = FetchType.LAZY)  // quando pegamos uma FotoProduto, nem sempre precisamos do produto
	@MapsId
	private Produto produto;

}
