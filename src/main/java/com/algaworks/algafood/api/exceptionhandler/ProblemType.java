package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	
	RECURSO_NAO_ENCONTRADA("Recurso não encontrda", "/recurso-nao-encontrada"),
	ENTIDADE_EM_USO("Entidade Em  Uso", "/entidade-em-uso"),
	ERRO_NEGOCIO("Violação de regra de negócio", "/erro-negocio"),
	MENSAGEM_INCOMPREESIVEL("Mensagem incompreensível", "/mensagem-inconpreensivel"),
	ERRO_DE_SISTEMA("Erro de sistema", "/erro-de-sistema"),
	DADOS_INVALIDOS("Dados inválidos", "/dados-invalidos"),
	PARAMETRO_INVALIDO("Parametro inválido", "/parametro-invalido");
	
	private String title;
	private String uri;
	
	private ProblemType(String title, String path) {
		this.title = title;
		this.uri = "https://www.algafood.com.br" + path;
	}

}
