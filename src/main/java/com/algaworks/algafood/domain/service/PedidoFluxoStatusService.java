package com.algaworks.algafood.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.model.Pedido;

@Service
public class PedidoFluxoStatusService {
	
	@Autowired
	PedidoCadastroService pedidoCadastro;
	
	@Transactional
	public void confirmar(String pedidoIdUnico) {
		Pedido pedido = pedidoCadastro.buscarOuFalhar(pedidoIdUnico);
		pedido.confirmar();
	}
	
	@Transactional
	public void entregar(String pedidoIdUnico) {
		Pedido pedido = pedidoCadastro.buscarOuFalhar(pedidoIdUnico);
		pedido.entregar();
	}
	
	@Transactional
	public void cancelar(String pedidoIdUnico) {
		Pedido pedido = pedidoCadastro.buscarOuFalhar(pedidoIdUnico);
		pedido.cancelar();
	}

}
