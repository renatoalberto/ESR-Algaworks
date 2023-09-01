package com.algaworks.algafood.domain.model;

public enum StatusPedido {
	
	CRIADO("Criado"),
	CONFIRMADO("Confirmado"),
	ENTREGUE("Entregue"),
	CANCELADO("Cancelado");
	
	private String descricao;
	
	StatusPedido(String texto) {
		this.descricao = texto;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	
	
}
