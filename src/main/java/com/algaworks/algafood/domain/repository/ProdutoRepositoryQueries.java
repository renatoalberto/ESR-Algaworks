package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {
	
	FotoProduto savarFoto(FotoProduto foto);
	
	void delete(FotoProduto foto);

}
