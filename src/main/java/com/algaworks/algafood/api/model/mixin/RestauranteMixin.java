package com.algaworks.algafood.api.model.mixin;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * classes de mixin para usar as anotações do Jackson 
 * configurada no com.algaworks.algafood.core.jackson.JacksonMixinModule
 * 
 * @author renat
 *
 */
public abstract class RestauranteMixin {
	
//	@JsonIgnore                               // Toda relação ToOne ocorre EagerLoad, gerando uma consulta (separada ou junta), independente do JsonIgnore
//	@JsonIgnoreProperties("hibernateLazyInitializer")  // Ignorar a propriedade do proxy de Cozinha na serialização do json 
	@JsonIgnoreProperties(value = "nome", allowGetters = true) // ignora nome na serialização, permite na desserealização
	private Cozinha cozinha;
	
	@JsonIgnore
	private OffsetDateTime dataCadastro;
//	private LocalDateTime dataCadastro;
	
	@JsonIgnore
	private OffsetDateTime dataAtualizacao;
//	private LocalDateTime dataAtualizacao;
	
	@JsonIgnore                           // Toda relação ToMany ocorre Lazy Load, consulta ocorre somente quando requisitado
	private List<FormaPagamento> formasPagamento = new ArrayList<FormaPagamento>();
	
	@JsonIgnore
	private Endereco endereco;
	
	@JsonIgnore
	private List<Produto> produtos;

}
