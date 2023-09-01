package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.output.GrupoDTO;
import com.algaworks.algafood.domain.model.Grupo;

@Component
public class GrupoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public GrupoDTO toGrupoDTO(Grupo grupo) {
		return modelMapper.map(grupo, GrupoDTO.class);
	}
	
	public Collection<GrupoDTO> collectionToDTO(Collection<Grupo> grupos) {
		return grupos.stream()
				.map(grupo -> toGrupoDTO(grupo))
				.collect(Collectors.toList());
	}

}
