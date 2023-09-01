package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.output.PedidoDTO;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public PedidoDTO toPedidoDTO(Pedido pedido) {
		return modelMapper.map(pedido, PedidoDTO.class);
	}
	
	public List<PedidoDTO> toCollectionPedidoDTO(List<Pedido> pedidos) {
		return pedidos.stream()
				.map(this::toPedidoDTO)
				.collect(Collectors.toList());
	}

}
