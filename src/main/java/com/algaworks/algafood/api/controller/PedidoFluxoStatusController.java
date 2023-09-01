package com.algaworks.algafood.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.service.PedidoFluxoStatusService;

@RestController
@RequestMapping("/pedidos/{pedidoId}")
public class PedidoFluxoStatusController {
	
	@Autowired
	PedidoFluxoStatusService pedidoFluxoService;
	
	@PutMapping("/confirmacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void confirmar(@PathVariable Long pedidoId) {
		pedidoFluxoService.confirmar(pedidoId);
	}
	
	@PutMapping("/entregua")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void entregar(@PathVariable Long pedidoId) {
		pedidoFluxoService.entregar(pedidoId);
	}
	
	@PutMapping("/cancelamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable Long pedidoId) {
		pedidoFluxoService.cancelar(pedidoId);
	}

}
