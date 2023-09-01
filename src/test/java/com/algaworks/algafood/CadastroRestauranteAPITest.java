package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteAPITest {
	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";

	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";
	
	private static final int RESTAURANTE_ID_INEXISTENTE = 100;

	@LocalServerPort
	private int port;
	
	private Restaurante restauranteExistente1;
	private Restaurante restauranteExistente2;
	private int quantidadeRestaurantes;
	
    private String jsonRestauranteCorreto;
    private String jsonRestauranteSemFrete;
    private String jsonRestauranteSemCozinha;
    private String jsonRestauranteComCozinhaInexistente;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@BeforeEach
	public void preparacaoInicial() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/restaurantes";
		
		this.jsonRestauranteCorreto = ResourceUtils.getContentFromResource(
				"/json/restaurante-new-york-barbecue.json");			
		this.jsonRestauranteSemFrete = ResourceUtils.getContentFromResource(
				"/json/restaurante-new-york-barbecue-sem-frete.json");			
		this.jsonRestauranteSemCozinha = ResourceUtils.getContentFromResource(
				"/json/restaurante-new-york-barbecue-sem-cozinha.json");			
		this.jsonRestauranteComCozinhaInexistente = ResourceUtils.getContentFromResource(
				"/json/restaurante-new-york-barbecue-com-cozinha-inexistente.json");			
		
		databaseCleaner.clearTables();
		preparaDados();
	}
	
	@Test
	public void deveRetornarStatus200_QuandoConsultaRestaurantes() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarRestaurante() {
		given()
			.body(this.jsonRestauranteCorreto)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteSemTaxaFrete() {
		given()
			.body(this.jsonRestauranteSemFrete)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteSemCozinha() {
		given()
			.body(this.jsonRestauranteSemCozinha)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
		.	post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}
	
	@Test
	public void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente() {
		given()
			.body(this.jsonRestauranteComCozinhaInexistente)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE))
			.body("detail", equalTo("Não foi encontrada cozinha com código 200"));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarRestauranteinexistente() {
		given()
			.pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("{restauranteId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveConter2Restaurantes() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("nome", hasItems(this.restauranteExistente1.getNome(), this.restauranteExistente2.getNome()))
			.body("", hasSize(this.quantidadeRestaurantes));
	}
	
	private void preparaDados() {
		// Cozinhas ------------------------------------
		Cozinha cozinha = new Cozinha();
		cozinha.setNome("Tailandesa");
		
		cozinha = cozinhaRepository.save(cozinha);
				
		// Estado  -------------------------------------
		Estado estado = new Estado();
		estado.setNome("Brasília");
		
		estado = estadoRepository.save(estado);
		
		// Cidade --------------------------------------
		Cidade cidade = new Cidade();
		cidade.setEstado(estado);
		cidade.setId(1L);
		cidade.setNome("Distrito Federal");
		
		cidade = cidadeRepository.save(cidade);

		// Endereco ------------------------------------
		Endereco endereco = new Endereco();
		endereco.setBairro("Rua do governador");
		endereco.setCep("71555-100");
		endereco.setCidade(cidade);
		endereco.setComplemento("qualquer coisa");
		endereco.setLogradouro("SMPW Quadra 17");
		endereco.setNumero("Número 22");
		
		// Formas de Pagamento -------------------------
		FormaPagamento formaPagamento1 = new FormaPagamento();
		formaPagamento1.setDescricao("Débito");
		formaPagamento1 = formaPagamentoRepository.save(formaPagamento1);
		
		FormaPagamento formaPagamento2 = new FormaPagamento();
		formaPagamento2.setDescricao("Crédito");
		formaPagamento2 = formaPagamentoRepository.save(formaPagamento2);
		
		Set<FormaPagamento> formasPagamento = new HashSet<FormaPagamento>();
		formasPagamento.add(formaPagamento1);		
		formasPagamento.add(formaPagamento2);
		
		// Restaurantes  -------------------------------		
		Restaurante restaurante1 = new Restaurante();
		restaurante1.setCozinha(cozinha);
		restaurante1.setEndereco(endereco);
		restaurante1.setFormasPagamento(formasPagamento);
		restaurante1.setNome("Coco Bambu");
		restaurante1.setProdutos(null);
		restaurante1.setTaxaFrete(new BigDecimal(15.0));
		
		Restaurante restaurante2 = new Restaurante();
		restaurante2.setCozinha(cozinha);
		restaurante2.setEndereco(endereco);
		restaurante2.setFormasPagamento(formasPagamento);
		restaurante2.setNome("Outback");
		restaurante2.setProdutos(null);
		restaurante2.setTaxaFrete(new BigDecimal(20.0));
		
		this.restauranteExistente1 = restauranteRepository.save(restaurante1);
		this.restauranteExistente2 = restauranteRepository.save(restaurante2);
		this.quantidadeRestaurantes = 2;
		
	};

}
