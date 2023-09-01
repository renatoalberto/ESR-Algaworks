package com.algaworks.algafood.domain.service;

import java.time.OffsetDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.NegocioExecption;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;

@Service
public class PedidoFluxoStatusService {
	
	private static final String MSG_ERRO_FLUXO_PEDIDO = "O pedido de código %d não pode ser alterado do status %s para o status %s";
	
	@Autowired
	PedidoCadastroService pedidoCadastro;
	
	@Transactional
	public void confirmar(Long pedidoId) {
		Pedido pedido = pedidoCadastro.buscarOuFalhar(pedidoId);
		
		if (!pedido.getStatus().equals(StatusPedido.CRIADO) && !pedido.getStatus().equals(StatusPedido.CONFIRMADO)) {
			throw new NegocioExecption(
					String.format(MSG_ERRO_FLUXO_PEDIDO, 
							pedidoId,
							pedido.getStatus().getDescricao(),
							StatusPedido.CONFIRMADO.getDescricao())
					);
		}
		
		if (pedido.getStatus().equals(StatusPedido.CRIADO)) {
			pedido.setStatus(StatusPedido.CONFIRMADO);
			pedido.setDataConfirmacao(OffsetDateTime.now());			
		}
		
	}
	
	@Transactional
	public void entregar(Long pedidoId) {
		Pedido pedido = pedidoCadastro.buscarOuFalhar(pedidoId);
		
		if (!pedido.getStatus().equals(StatusPedido.CONFIRMADO) && !pedido.getStatus().equals(StatusPedido.ENTREGUE)) {
			throw new NegocioExecption(
					String.format(MSG_ERRO_FLUXO_PEDIDO, 
							pedidoId,
							pedido.getStatus().getDescricao(),
							StatusPedido.ENTREGUE.getDescricao())
					);
		}
		
		if (pedido.getStatus().equals(StatusPedido.CONFIRMADO)) {
			pedido.setStatus(StatusPedido.ENTREGUE);
			pedido.setDataConfirmacao(OffsetDateTime.now());			
		}
		
	}
	
	@Transactional
	public void cancelar(Long pedidoId) {
		Pedido pedido = pedidoCadastro.buscarOuFalhar(pedidoId);
		
		if (!pedido.getStatus().equals(StatusPedido.CRIADO) && !pedido.getStatus().equals(StatusPedido.CANCELADO)) {
			throw new NegocioExecption(
					String.format(MSG_ERRO_FLUXO_PEDIDO, 
							pedidoId,
							pedido.getStatus().getDescricao(),
							StatusPedido.CANCELADO.getDescricao())
					);
		}
		
		if (pedido.getStatus().equals(StatusPedido.CRIADO)) {
			pedido.setStatus(StatusPedido.CANCELADO);
			pedido.setDataConfirmacao(OffsetDateTime.now());			
		}
		
	}

}
