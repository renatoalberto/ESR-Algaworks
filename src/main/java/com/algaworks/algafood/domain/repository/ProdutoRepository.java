package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;

public interface ProdutoRepository extends JpaRepositoryImplementation<Produto, Long>, ProdutoRepositoryQueries {

	public List<Produto> findByRestaurante(Restaurante restaurante);
	
	@Query("from Produto p where p.ativo = true and p.restaurante = :restaurante")
	public List<Produto> findAtivosByRestaurante(Restaurante restaurante);
	
	@Query("select f "
			+ "from FotoProduto f "
			+ "join f.produto p "
			+ "where p.restaurante.id = :restauranteId "
			+ "and f.produto.id = :produtoId")
	Optional<FotoProduto> findFotoById(Long restauranteId, Long produtoId);
	
}
