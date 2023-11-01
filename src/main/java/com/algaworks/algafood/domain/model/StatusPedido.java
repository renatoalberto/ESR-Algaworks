package com.algaworks.algafood.domain.model;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {
	
	CRIADO("Criado"),
	CONFIRMADO("Confirmado", CRIADO),
	ENTREGUE("Entregue", CONFIRMADO),
	CANCELADO("Cancelado", CRIADO);
	
	private String descricao;
	private List<StatusPedido> statusAnterioresPermitido;
	
	// com varargs (...) podemos passar nenhum, um ou mais de um
	StatusPedido(String texto, StatusPedido... statusAnterioresPermitido) {
		this.descricao = texto;
		this.statusAnterioresPermitido = Arrays.asList(statusAnterioresPermitido);
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public boolean naoPodeAlterarPara(StatusPedido novoStatus) {
		return !novoStatus.statusAnterioresPermitido.contains(this);
	}
	
	
	
}
