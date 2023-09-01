package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import com.algaworks.algafood.domain.model.Cozinha;

public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long> {
	
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
	 * Pesquisa por nome
	 * 
	 * É possível utilizar - (nome, findByNome, findTodasByNome)
	 * 
	 * @param nome
	 * @return Optional<List<Cozinha>>
	 */
	Optional<List<Cozinha>> findTodasByNome(String nome);
	
	/**
	 * Pesquisa por parte do nome de cozinha que contenha o nome informado
	 * 
	 * @param nome
	 * @return Optional<List<Cozinha>>
	 */ 
	Optional<List<Cozinha>> findTodasByNomeContaining(String nome);

	/**
	 * Pesquisa por parte do nome de cozinha e retorna o primeiro registro da lista
	 * 
	 * @param nome
	 * @return Optional<Cozinha>
	 */ 
	Optional<Cozinha> findFirstByNomeContaining(String nome);
	
	/**
	 * Pesquisa por parte do nome de cozinha e retorna os 2 primeiros registro da lista
	 * 
	 * @param nome
	 * @return Optional<List<Cozinha>>
	 */ 
	Optional<List<Cozinha>> findTop2ByNomeContaining(String nome);
	
	/**
	 * Verifica se parte do nome informado existe em nomes de cozinha
	 * 
	 * @param nome
	 * @return Optional<List<Cozinha>>
	 */ 
	boolean existsByNomeContaining(String nome);
	
	/**
	 * Contador de parte do nome informado 
	 * 
	 * @param nome
	 * @return int
	 */ 
	int countByNomeContaining(String nome);

}
