package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.RestauranteInputDTO;
import com.algaworks.algafood.api.model.output.RestauranteDTO;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler {
	@Autowired
	ModelMapper modelMapper;
	
	public RestauranteInputDTO toRestauranteDTO(Restaurante restaurante) {
		
//		CozinhaInputIdDTO cozinhaInput = new CozinhaInputIdDTO();
//		cozinhaInput.setId(restaurante.getCozinha().getId());
//		
//		RestauranteInputDTO restauranteInput = new RestauranteInputDTO();
//		restauranteInput.setNome(restaurante.getNome());
//		restauranteInput.setTaxaFrete(restaurante.getTaxaFrete());
//		restauranteInput.setCozinha(cozinhaInput);
//		return restauranteInput;

		return modelMapper.map(restaurante, RestauranteInputDTO.class);
	}	

	public RestauranteDTO toDTO(Restaurante restaurante) {
//		CozinhaDTO cozinhaDTO = new CozinhaDTO();
//		cozinhaDTO.setId(restaurante.getCozinha().getId());
//		cozinhaDTO.setNome(restaurante.getCozinha().getNome());
//		
//		RestauranteDTO restauranteDTO = new RestauranteDTO();
//		restauranteDTO.setId(restaurante.getId());
//		restauranteDTO.setNome(restaurante.getNome());
//		restauranteDTO.setTaxaFrete(restaurante.getTaxaFrete());
//		restauranteDTO.setCozinha(cozinhaDTO);
//		return restauranteDTO;
		
		return modelMapper.map(restaurante, RestauranteDTO.class);
	}
	
	public List<RestauranteDTO> toCollectionDTO(List<Restaurante> restaurantes) {
		return restaurantes.stream()
				.map(restaurante -> toDTO(restaurante))
				.collect(Collectors.toList());
	}
}
