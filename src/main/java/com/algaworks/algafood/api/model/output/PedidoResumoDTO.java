package com.algaworks.algafood.api.model.output;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.algaworks.algafood.domain.model.StatusPedido;
//import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Getter;
import lombok.Setter;

//@JsonFilter("pedidoFiltro")  // Limitando os campos retornados pela API com @JsonFilter do Jackson
@Getter
@Setter
public class PedidoResumoDTO {

	private String codigo;
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;	
	private BigDecimal valorTotal;
	private StatusPedido statusPedido;
	private OffsetDateTime dataCriacao;
	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataCancelamento;
	private OffsetDateTime dataEntrega;	
	private RestauranteResumoDTO restaurante;
	private UsuarioDTO cliente;
	
}
