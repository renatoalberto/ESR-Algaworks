package com.algaworks.algafood.infrastructure.repository.removidos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cozinha;
//import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Repository
public class CozinhaRepositoryImplementacao { // implements CozinhaRepository {

	@PersistenceContext
	private EntityManager manager;
	
//	@Override
	public List<Cozinha> listar() {
		TypedQuery<Cozinha> query = manager.createQuery("from Cozinha", Cozinha.class);
		
		return query.getResultList();
	}
	
//	@Override
	public List<Cozinha> listarPorNome(String nome) {
		return manager.createQuery("from Cozinha where nome like :nome", Cozinha.class)
				.setParameter("nome", "%" + nome + "%")
				.getResultList();
	}
	
//	@Override
	public Cozinha buscar(Long id) {
		return manager.find(Cozinha.class, id);
	}
	
	/**
	 * Método salvarCozinha permite:</br>
	 *   - inserir um nova cozinha;</br>
	 *   - atualizar a cozinha no banco de dados quando o id já existir
	 * 
	 * @param cozinha
	 * @return
	 */
//	@Override
	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return manager.merge(cozinha);
	}
	
//	@Override
	@Transactional
	public void remover(Long cozinhaId) {
		Cozinha cozinha = this.buscar(cozinhaId);
		
		if (cozinha == null) {
			throw new EmptyResultDataAccessException(1);  // Parametro 1 representa a quantidade de registros esperados 
		}

		manager.remove(cozinha);			
	}


}
