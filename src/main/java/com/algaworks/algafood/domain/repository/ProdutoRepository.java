package com.algaworks.algafood.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;

public interface ProdutoRepository extends JpaRepositoryImplementation<Produto, Long> {

	public List<Produto> findByRestaurante(Restaurante restaurante);
	
}
