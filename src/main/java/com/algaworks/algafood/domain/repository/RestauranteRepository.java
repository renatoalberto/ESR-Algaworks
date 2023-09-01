package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.algaworks.algafood.domain.model.Restaurante;

public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries, JpaSpecificationExecutor<Restaurante> {

	/**
	 * Utiliza Query Method do Spring Data JPA
	 *   Documentação - https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	 *   Interpreta os nomes dos métodos e cria consultas conforme critérios informados 
	 * 
	 * Iniciamos as Query Method de consulta com:
	 * 1ª- find                 
	 * 2ª- read
	 * 3ª- get
	 * 4ª- query
	 * 5ª- stream
	 * 
	 * Prefixos Query Method utilitários
	 * 1ª - exists     - retorna um boolean 
	 * 2ª - count      - retorna um inteiro
	 * 
	 * Flags Query Method estudadas
	 * 1ª - Containing - palavra reservada que representa o like
	 * 2ª - Between    - representa entre 
	 * 3ª - And        - permite utilizar mais de uma condição
	 * 4ª - First      - retorna somente o primeiro registro da lista
	 * 5ª - TopN       - retorna quantidade de registros informado em N
	 */
	
	/**
	 * Mudando a implementação do findAll utilizando @Query
	 * utilizando join para evitar multiplos selects
	 * com um único select  ** Muitooo bonito
	 */
	@Query("from Restaurante r join fetch r.cozinha")
	public List<Restaurante> findAll();
	
	/** 
	 * Lista restaurantes com taxa frete entre os valores inicial e final informados
	 * 
	 * 
	 * @param taxaInicial
	 * @param taxaFinal
	 * @return List<Restaurante>
	 */
	public List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);
	
	/**
	 * Pesquisa por nome do restaurante e o id da cozinha 
	 * 
	 * @param nome
	 * @param cozinhaId
	 * @return List<Restaurante>
	 */
	public List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);
	
	/**
	 * Pesquisa por nome do restaurante e o id da cozinha 
	 * 
	 * @param nome
	 * @param cozinhaId
	 * @return List<Restaurante>
	 */
	@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
	public List<Restaurante> consultaNomeRestauranteQuery(String nome, @Param("id") Long cozinhaId);
	
	/**
	 * Externalizando consultas JPQL para um arquivo XML
	 * 
	 * Pesquisa por nome do restaurante e o id da cozinha 
	 * 
	 * @param nome
	 * @param cozinhaId
	 * @return List<Restaurante>
	 */
	public List<Restaurante> consultaNomeRestauranteXml(String nome, @Param("id") Long cozinhaId);
	
	/**
	 * Implementação customizada na classe RestauranteRepositoryImpl
	 * @param nome
	 * @param taxaFreteInicial
	 * @param taxaFreteFinal
	 * @return List<Restaurante> 
	 */
	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

	/**
	 * Implementação com Criteria API
	 * @param nome
	 * @param taxaFreteInicial
	 * @param taxaFreteFinal
	 * @return List<Restaurante> 
	 */
	List<Restaurante> findCriteriaQuery(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);
}
