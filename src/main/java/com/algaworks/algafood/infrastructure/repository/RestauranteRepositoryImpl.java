package com.algaworks.algafood.infrastructure.repository;

import static com.algaworks.algafood.infrastructure.repository.especification.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infrastructure.repository.especification.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	@Lazy            // evitar erro de referencia circular, @Lazy indica que a injeção ocorrerá somente quando necessário
	private RestauranteRepository restauranteRepository;
	
	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		
		var jpql = new StringBuilder();

		jpql.append("from Restaurante where 0 = 0 ");
		
		if (StringUtils.hasLength(nome)) {
			jpql.append("and nome like :nome ");
			parametros.put("nome", "%" + nome + "%");
		}
		
		if (taxaFreteInicial != null) {
			jpql.append("and taxaFrete >= :taxaInicial ");			
			parametros.put("taxaInicial", taxaFreteInicial);
		}
		
		if (taxaFreteFinal != null) {
			jpql.append("and taxaFrete <= :taxaFinal");			
			parametros.put("taxaFinal", taxaFreteFinal);
		}
		
		TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);
		
		parametros.forEach((chave, valor) -> { query.setParameter(chave, valor); });
		
		return query.getResultList();
		
		
//			.setParameter("nome", "%" + nome + "%")
//			.setParameter("taxaInicial", taxaFreteInicial)
//			.setParameter("taxaFinal", taxaFreteFinal)
//			.getResultList();
		
	}

	@Override
	public List<Restaurante> findCriteriaQuery(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder(); 
		
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
		
		Root<Restaurante> root = criteria.from(Restaurante.class); 
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (StringUtils.hasLength(nome)) {
			Predicate nomePredicate = builder.like(root.get("nome"), "%" + nome + "%");
			predicates.add(nomePredicate);
		}
		
		if (taxaFreteInicial != null) {
			Predicate taxaInicialPredicate = builder.greaterThanOrEqualTo(root.get("taxaFrete"),taxaFreteInicial);
			predicates.add(taxaInicialPredicate);
		}
		
		if (taxaFreteFinal != null) {
			Predicate taxaFinalPredicate = builder.lessThanOrEqualTo(root.get("taxaFrete"),taxaFreteFinal);
			predicates.add(taxaFinalPredicate);
		}
		
//		criteria.where(nomePredicate, taxaInicialPredicate, taxaFinalPredicate);
		criteria.where(predicates.toArray(new Predicate[0]));
		
		TypedQuery<Restaurante> query = manager.createQuery(criteria);
		
		return query.getResultList();
	}

	@Override
	public List<Restaurante> findRestauranteComFreteGratis(String nome) {
		return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
	}

}
