package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.output.PedidoResumoDTO;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoResumoModelDTO {

	@Autowired
	private ModelMapper modelMapper;
	
	public PedidoResumoDTO toPedidoResumoDTO(Pedido pedido) {
		return modelMapper.map(pedido, PedidoResumoDTO.class);
	}
	
	public List<PedidoResumoDTO> toCollectionPedidoResumoDTO(List<Pedido> pedidos) {
		return pedidos.stream()
				.map(this::toPedidoResumoDTO)
				.collect(Collectors.toList());
	}
	
}
