package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaApiTest {
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	private int quantidadeCozinhasCadastrada;
	private Cozinha cozinhaExistente1;
	private Cozinha cozinhaExistente2;
	
	private String jsonCorretoCozinhaChinesa;
	
	private Cozinha cozinhaInexistente;
	
//	@Autowired
//	private Flyway flyway;
	
	@BeforeEach
	public void preparacaoInicial() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();		
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		
		this.jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource(
				"/json/cozinha-chinesa.json");	
		
		this.cozinhaInexistente = new Cozinha();
		this.cozinhaInexistente.setId(9999L);
		
//		Para execução do script: \src\main\resources\db\dataDesenvolvimento\afterMigrate.sql
//		flyway.migrate();
		
		databaseCleaner.clearTables();
		prepararDados();
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
		given()		
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveConter2Cozinhas_QuandoConsultarCozinhas() {
		given()		
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(this.quantidadeCozinhasCadastrada))
			.body("nomeDaCozinha", hasItems(this.cozinhaExistente1.getNome(), this.cozinhaExistente2.getNome()));
	}
	
	@Test
	public void testRetornarStatus201_QuandoCadastrarCozinha() {
		given()
			.body(this.jsonCorretoCozinhaChinesa)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarStatusCorreto_QuandoConsultarCozinhaExistente() {
		given()	
			.pathParam("cozinhaId", this.cozinhaExistente1.getId())
			.accept(ContentType.JSON)
		.when()
			.get("{cozinhaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nomeDaCozinha", equalTo(this.cozinhaExistente1.getNome()));
	}
	
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		given()	
			.pathParam("cozinhaId", cozinhaInexistente.getId())
			.accept(ContentType.JSON)
		.when()
			.get("{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	private void prepararDados() {
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Tailandesa");
		
		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Indiana");
		
		this.cozinhaExistente1 = cozinhaRepository.save(cozinha1);
		this.cozinhaExistente2 = cozinhaRepository.save(cozinha2);
		
		this.quantidadeCozinhasCadastrada = 2;
	}


}
