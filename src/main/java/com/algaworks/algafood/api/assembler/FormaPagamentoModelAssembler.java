package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.output.FormaPagamentoDTO;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public FormaPagamentoDTO toFormaPagamentoDTO(FormaPagamento formaPagamento) {
		return modelMapper.map(formaPagamento, FormaPagamentoDTO.class);
	}
	
	public Collection<FormaPagamentoDTO> toCollectionDTO(Collection<FormaPagamento> formasPagamento) {
		return formasPagamento.stream()
				.map(formaPagamento -> toFormaPagamentoDTO(formaPagamento))
				.collect(Collectors.toList());
	}

}
