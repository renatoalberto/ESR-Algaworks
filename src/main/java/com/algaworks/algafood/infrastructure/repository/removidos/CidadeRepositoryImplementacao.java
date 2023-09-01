package com.algaworks.algafood.infrastructure.repository.removidos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cidade;
//import com.algaworks.algafood.domain.repository.CidadeRepository;

//@Repository
public class CidadeRepositoryImplementacao { // implements CidadeRepository{
	
	@PersistenceContext
	private EntityManager manager;

//	@Override
	public List<Cidade> listar() {
		return manager.createQuery("from Cidade", Cidade.class).getResultList();
	}

//	@Override
	public Cidade buscar(Long id) {
		return manager.find(Cidade.class, id);
	}

//	@Override
	@Transactional
	public Cidade salvar(Cidade cidade) {
		return manager.merge(cidade);
	}

//	@Override
	@Transactional
	public void remover(Long cidadeId) {
		Cidade cidade = this.buscar(cidadeId);
		manager.remove(cidade);
	}
	
	

}
