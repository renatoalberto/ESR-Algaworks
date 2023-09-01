package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.algaworks.algafood.core.validation.Groups;
import com.algaworks.algafood.core.validation.TaxaFrete;
import com.algaworks.algafood.core.validation.ValorZeroIncluiDescricao;

import lombok.Data;

@Data
@Entity
@Table
@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome", descricaoObrigatoria = "Frete Grátis" )
public class Restaurante {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@NotNull
//	@NotEmpty
	@NotBlank  // (message = "Informe um nome de restaurante válido.")
	@Column(nullable = false)
	private String nome;
	
//	@DecimalMin("0")
	@NotNull
//	@PositiveOrZero  // (message = "{taxaFrete.invalida}")
	@TaxaFrete
//	@Multiplo(numero = 5)
	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;
	
	@Valid                                    // Para validação de uma entidade em cascata
	@ConvertGroup(from = Default.class, to = Groups.CozinhaId.class)
	@NotNull
	@ManyToOne  // (fetch = FetchType.LAZY)        // Mudamos o padrão para LAZY, buscando a cozinha somente quando demandado 
	@JoinColumn(name = "cozinha_id", nullable = false)
	private Cozinha cozinha = new Cozinha();
	
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime(6)")  // datetime(6) representa um timestamp com 6 casas de milessegundos - *padrão
	private OffsetDateTime dataCadastro;
//	private LocalDateTime dataCadastro;
	
	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime(6)")
	private OffsetDateTime dataAtualizacao;
//	private LocalDateTime dataAtualizacao;
	
	private Boolean ativo = Boolean.TRUE;
	
	private Boolean aberto = Boolean.TRUE;
	
	@ManyToMany // (fetch = FetchType.EAGER)  // Mudamos o padrão para Eager, consulta ansiosa *** Usar com moderação, pouco usado, busca formas de pagamento mesmo não precisando  
	@JoinTable(name = "restaurante_formas_pagamento",
			joinColumns = @JoinColumn(name= "restaurante_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id", referencedColumnName = "id"))
	private Set<FormaPagamento> formasPagamento = new HashSet<FormaPagamento>();
	
	@Embedded            // indica que é um tipo incorporada, não existe uma entidade endereco no banco de dados
	private Endereco endereco = new Endereco();
	
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name="restaurante_usuario_responsavel",
			joinColumns = @JoinColumn(name="restaurante_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"))
	private Set<Usuario> responsaveis = new HashSet<Usuario>();
	
	public void ativar() {
		this.setAtivo(true);
	}
	
	public void inativar() {
		this.setAtivo(false);
	}
	
	public void abrir() {
		this.setAberto(true);
	}
	
	public void fechar() {
		this.setAberto(false);
	}
	
	public void adicionarResponsavel(Usuario responsavel) {
		getResponsaveis().add(responsavel);
	}
	
	public void removerResponsavel(Usuario responsavel) {
		getResponsaveis().remove(responsavel);
	}
	
}
