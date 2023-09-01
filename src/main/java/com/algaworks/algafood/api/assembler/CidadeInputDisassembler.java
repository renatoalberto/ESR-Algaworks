package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.CidadeInputDTO;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeInputDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Cidade toDomainCidade(CidadeInputDTO cidadeInput) {
		return modelMapper.map(cidadeInput, Cidade.class);
	}
	
	public void copyToDomainCidade(CidadeInputDTO cidadeDTO, Cidade cidade) {
		// Evitar o erro  org.hibernate.HibernateException: identifier of an instance of com.algaworks.algafood.domain.model.Estado 
		// was altered from 1 to 2
		cidade.setEstado(null);
		
		modelMapper.map(cidadeDTO, cidade);
	}

}
