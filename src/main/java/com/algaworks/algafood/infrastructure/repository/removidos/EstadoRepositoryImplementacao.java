package com.algaworks.algafood.infrastructure.repository.removidos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Estado;
//import com.algaworks.algafood.domain.repository.EstadoRepository;

//@Repository
public class EstadoRepositoryImplementacao { // implements EstadoRepository {
	
	@PersistenceContext
	private EntityManager manager;

//	@Override
	public List<Estado> listar() {
		return manager.createQuery("from Estado", Estado.class).getResultList();
	}

//	@Override
	public Estado buscar(Long id) {
		return manager.find(Estado.class, id);
	}

//	@Override
	@Transactional
	public Estado salvar(Estado estado) {
		return manager.merge(estado);
	}

//	@Override
	@Transactional
	public void remover(Long estadoId) {
		Estado estado = this.buscar(estadoId);
		
		if (estado == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		manager.remove(estado);			
	}

}
