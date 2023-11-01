package com.algaworks.algafood.api.model.output;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.algaworks.algafood.domain.model.StatusPedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDTO {

	private String codigo;
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	private OffsetDateTime dataCriacao;
	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataCancelamento;
	private OffsetDateTime dataEntrega;	
	private FormaPagamentoDTO formaPagamento;
	private RestauranteResumoDTO restaurante;
	private UsuarioDTO cliente;
	private EnderecoDTO enderecoEntrega;
	private StatusPedido statusPedido;
	private List<ItemPedidoDTO> itens = new ArrayList<>();	
	
}
