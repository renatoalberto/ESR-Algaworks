package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.input.ItemPedidoInputDTO;
import com.algaworks.algafood.api.model.output.EnderecoDTO;
import com.algaworks.algafood.api.model.output.RestauranteDTO;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.ItemPedido;
import com.algaworks.algafood.domain.model.Restaurante;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		// Ignorar a atribuição do id no controlador PedidoController.salvar
		// estava atribuindo o produtoId para o id do ItemPedido indevidamente
		modelMapper.createTypeMap(ItemPedidoInputDTO.class, ItemPedido.class)
			.addMappings(mapper -> mapper.skip(ItemPedido::setId));
		
		// Configuração de mapeamento para o nome do estado, dentro do EnderecoDTO
		modelMapper.createTypeMap(Endereco.class, EnderecoDTO.class)
		.<String>addMapping(
				enderecoOrg -> enderecoOrg.getCidade().getEstado().getNome(),
				(enderecoDst, value) -> enderecoDst.getCidade().setEstado(value));
		
		// Configuração de mapeamento que não foi possível dar match
		modelMapper.createTypeMap(Restaurante.class, RestauranteDTO.class)
			.addMapping(Restaurante::getTaxaFrete, RestauranteDTO::setPrecoFrete);
		
		return modelMapper;
	}

}
