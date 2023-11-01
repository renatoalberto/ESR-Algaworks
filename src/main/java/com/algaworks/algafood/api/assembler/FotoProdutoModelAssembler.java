package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.output.FotoProdutoDTO;
import com.algaworks.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public FotoProdutoDTO toFotoProdutoDTO(FotoProduto fotoProduto) {
		return modelMapper.map(fotoProduto, FotoProdutoDTO.class);
	}
	
	public FotoProdutoDTO toDTO(FotoProduto foto) {
		return modelMapper.map(foto, FotoProdutoDTO.class);		
	}

}
