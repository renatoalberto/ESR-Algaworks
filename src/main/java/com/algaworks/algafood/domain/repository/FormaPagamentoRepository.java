package com.algaworks.algafood.domain.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.algaworks.algafood.domain.model.FormaPagamento;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {
	
	@Query("select max(dataAtualizacao) from FormaPagamento")
	OffsetDateTime getDataUltimaAtualizacao();
	
	@Query("select f.dataAtualizacao "
			+ "from FormaPagamento f "
			+ "where f.id = :formaPagamentoId")
	OffsetDateTime findDataUltimaAtualizacaoById(Long formaPagamentoId);

}
