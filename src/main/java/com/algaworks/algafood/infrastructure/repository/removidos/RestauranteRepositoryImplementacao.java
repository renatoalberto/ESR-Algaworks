package com.algaworks.algafood.infrastructure.repository.removidos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Restaurante;
//import com.algaworks.algafood.domain.repository.RestauranteRepository;

//@Repository
public class RestauranteRepositoryImplementacao { // implements RestauranteRepository {
	
	@PersistenceContext
	private EntityManager manager;

//	@Override
	public List<Restaurante> listar() {
		TypedQuery<Restaurante> query =  manager.createQuery("from Restaurante", Restaurante.class);
		return query.getResultList();
	}

//	@Override
	public Restaurante buscar(long id) {
		return manager.find(Restaurante.class, id);
	}

	/**
	 * Permite inserir e atualizar uma cozinha
	 */
//	@Override
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		return manager.merge(restaurante);
	}

//	@Override
	@Transactional
	public void remover(Long restauranteId) {
		Restaurante restaurante = this.buscar(restauranteId);
		manager.remove(restaurante);
	}

}
