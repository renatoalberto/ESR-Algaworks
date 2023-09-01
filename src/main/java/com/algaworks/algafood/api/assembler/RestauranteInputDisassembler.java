package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.RestauranteInputDTO;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Restaurante toDomainRestaurante(RestauranteInputDTO restauranteInput) {
//		Cozinha cozinha = new Cozinha();
//		cozinha.setId(restauranteInput.getCozinha().getId());
//		
//		Restaurante restaurante = new Restaurante();
//		restaurante.setNome(restauranteInput.getNome());
//		restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());
//		restaurante.setCozinha(cozinha);
//		
//		return restaurante;
		
		return modelMapper.map(restauranteInput, Restaurante.class);
	}
	
	public void copyToDomainRestaurante(RestauranteInputDTO restauranteInput, Restaurante restaurante) {
		// evitar erro org.hibernate.HibernateException: identifier of an instance 
		// of com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
		restaurante.setCozinha(new Cozinha());
		restaurante.getEndereco().setCidade(new Cidade());
		
		modelMapper.map(restauranteInput, restaurante);
	}
	
}
