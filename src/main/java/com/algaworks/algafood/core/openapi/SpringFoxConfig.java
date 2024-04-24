package com.algaworks.algafood.core.openapi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.fasterxml.classmate.TypeResolver;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Representation;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)                // 18.11. Descrevendo restrições de validação de propriedades do modelo
public class SpringFoxConfig {
	
	// Utilize a URL http://localhost:8080/v3/api-docs para acessar o JSON de configuração na versão 3.
	// Acessando a documentação Swagger em html: http://localhost:8080/swagger-ui/index.html

	@Bean
	public Docket apiDocket() {
		var typeResolver = new TypeResolver();   
		
		// Docket representa um sumário, como queremos gerar o json para documentação 
		return new Docket(DocumentationType.OAS_30)
				.select()
//					.apis(RequestHandlerSelectors.any())                                        // selecionar quaquer controlador, nao queremos qualquer um
					.apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))    // queremos apenas os controladores do pacote informado
//					.paths(PathSelectors.ant("/restaurantes/*"))                                // somentes o path informado
					.build()
				.useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET, globalGetResponseMessages())
				.globalResponses(HttpMethod.POST, globalPostResponseMessages())
				.globalResponses(HttpMethod.PUT, globalPutResponseMessages())
				.globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
				.globalResponses(HttpMethod.GET, globalGetResponseMessages())
				.additionalModels(typeResolver.resolve(Problem.class))
				.apiInfo(apiInfo())
				.tags(new Tag("Cidades", "Gerencia as cidades"));
	}
	
	private ApiInfo apiInfo() {
		
		return new ApiInfoBuilder()
				.title("AlgaFood API")
				.description("API aberta para clientes e restaurantes")
				.version("2,3")
				.contact(new Contact("AlgaWorks", "https://www.algaworks.com", "contato@algaworks.com"))
				.build();
				
	}
	
	// Somente para códigos de erro. (Classe 400 ou 500)
	// Nos códigos de sucesso o SpringFox dará preferência ao que encontra no método 
	private List<Response> globalGetResponseMessages() {
		return Arrays.asList(
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description("Erro interno do servidor")
						.representation( MediaType.APPLICATION_JSON)                // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
						.apply(getProblemaModelReference())                         // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3						
						.build(),
					new ResponseBuilder()
						.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
						.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
						.build()
					
				);
	}
	
	// Somente para códigos de erro. (Classe 400 ou 500)
	// Nos códigos de sucesso o SpringFox dará preferência ao que encontra no método 
	private List<Response> globalPostResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Erro de requisição do cliente")
				.representation( MediaType.APPLICATION_JSON)                // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.apply(getProblemaModelReference())                         // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.representation( MediaType.APPLICATION_JSON)                // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.apply(getProblemaModelReference())                         // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
				.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
				.description("Formato do payload não é um formato suportado")
				.representation( MediaType.APPLICATION_JSON)                // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.apply(getProblemaModelReference())                         // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.build()
				
				);
	}
	
	// Somente para códigos de erro. (Classe 400 ou 500)
	// Nos códigos de sucesso o SpringFox dará preferência ao que encontra no método 
	private List<Response> globalPutResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Erro de requisição do cliente")
				.representation( MediaType.APPLICATION_JSON)                // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.apply(getProblemaModelReference())                         // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.representation( MediaType.APPLICATION_JSON)                // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.apply(getProblemaModelReference())                         // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
				.description("Recurso não possui representação que poderia ser aceita pelo consumidor")
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
				.description("Formato do payload não é um formato suportado")
				.representation( MediaType.APPLICATION_JSON)                // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.apply(getProblemaModelReference())                         // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.build()
				
				);
	}
	
	// Somente para códigos de erro. (Classe 400 ou 500)
	// Nos códigos de sucesso o SpringFox dará preferência ao que encontra no método 
	private List<Response> globalDeleteResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
				.description("Erro de requisição do cliente")
				.representation( MediaType.APPLICATION_JSON)                // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.apply(getProblemaModelReference())                         // 18.15. Referenciando modelo de representação de problema com códigos de status de erro - SpringFox 3.0 e Open API 3
				.build(),
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.description("Erro interno do servidor")
				.build()
				
				);
	}
	
	
	// 18.15. Referenciando modelo de representação de problema com códigos de status de erro
	private Consumer<RepresentationBuilder> getProblemaModelReference() {
		return r -> r.model(m -> m.name("Problema")
				.referenceModel(ref -> ref.key(k -> k.qualifiedModelName(q -> q.name("Problema").namespace("com.algaworks.algafood.api.exceptionhandler")))));
	}
	
}
