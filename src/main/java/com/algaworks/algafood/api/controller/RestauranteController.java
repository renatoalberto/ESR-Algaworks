package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.model.input.RestauranteInputDTO;
import com.algaworks.algafood.api.model.output.RestauranteDTO;
import com.algaworks.algafood.api.model.view.RestauranteView;
import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaExecption;
import com.algaworks.algafood.domain.exception.NegocioExecption;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.RestauranteCadastroService;
import com.algaworks.algafood.infrastructure.repository.especification.RestauranteComFreteGratisSpec;
import com.algaworks.algafood.infrastructure.repository.especification.RestauranteComNomeSemelhante;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private RestauranteCadastroService restauranteCadastro;
	
	@Autowired
	private SmartValidator validator;
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;

	@GetMapping
	public MappingJacksonValue listarProjecao(@RequestParam(required = false) String projecao) {
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		List<RestauranteDTO> restauranteDTO = restauranteModelAssembler.toCollectionDTO (restaurantes);
		
		MappingJacksonValue restauranteWrapper = new MappingJacksonValue(restauranteDTO);   // Fazendo projeção de recursos com @JsonView do Jackson
		
		restauranteWrapper.setSerializationView(RestauranteView.Resumo.class);              
		
		if ("apenas-nome".equals(projecao)) {
			restauranteWrapper.setSerializationView(RestauranteView.ApenasNome.class);
		} else if ("completo".equals(projecao)) {
			restauranteWrapper.setSerializationView(null);
		}
		
		return restauranteWrapper;
	}
	
//	@GetMapping(params= "projecao=completo")
//	public ResponseEntity<List<RestauranteDTO>> listarCompleto() {
//		return listar();
//	}
//	
//	@GetMapping(params= "projecao=resumo")
//	@JsonView(RestauranteView.Resumo.class)        // Fazendo projeção de recursos com @JsonView do Jackson
//	public ResponseEntity<List<RestauranteDTO>> listarResumido() {
//		return listar();
//	}
//	
//	@GetMapping(params= "projecao=apenas-nome")
//	@JsonView(RestauranteView.ApenasNome.class)        // Fazendo projeção de recursos com @JsonView do Jackson
//	public ResponseEntity<List<RestauranteDTO>> listarApenasNome() {
//		return listar();
//	}
	
	@GetMapping("/{id}")
	public RestauranteDTO buscar(@PathVariable("id") Long restauranteId) {
		Restaurante restaurante = restauranteCadastro.buscarOuFalhar(restauranteId);
		
		return restauranteModelAssembler.toDTO(restaurante);
	}

	@GetMapping("/por-taxa-frete")
	public ResponseEntity<List<RestauranteDTO>> listarRestaurantesPorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal) {
		List<Restaurante> restaurantes = restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
		
		if (restaurantes.size() > 0) {
			return ResponseEntity.ok(restauranteModelAssembler.toCollectionDTO(restaurantes));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/por-nome-cozinha")
	public ResponseEntity<List<RestauranteDTO>> listarRestaurantePorNomeCozinha(String nome, Long cozinha) {
		List<Restaurante> restaurantes = restauranteRepository.findByNomeContainingAndCozinhaId(nome, cozinha);
		
		if (restaurantes.size() > 0) {
			return ResponseEntity.ok(restauranteModelAssembler.toCollectionDTO(restaurantes));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/por-nome-cozinha-query")
	public ResponseEntity<List<RestauranteDTO>> listarRestaurantePorNomeCozinhaQuery(String nome, Long cozinha) {
		List<Restaurante> restaurantes = restauranteRepository.consultaNomeRestauranteQuery(nome, cozinha);
		
		if (restaurantes.size() > 0) {
			return ResponseEntity.ok(restauranteModelAssembler.toCollectionDTO(restaurantes));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/por-nome-cozinha-xml")
	public ResponseEntity<List<RestauranteDTO>> listarRestaurantePorNomeCozinhaXml(String nome, Long cozinha) {
		List<Restaurante> restaurantes = restauranteRepository.consultaNomeRestauranteXml(nome, cozinha);
		
		if (restaurantes.size() > 0) {
			return ResponseEntity.ok(restauranteModelAssembler.toCollectionDTO(restaurantes));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/por-nome-taxa-customizado")
	public ResponseEntity<List<RestauranteDTO>> listarRestaurantePorNomeTaxaCustomizado(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		List<Restaurante> restaurantes = restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
		
		if (restaurantes.size() > 0) {
			return ResponseEntity.ok(restauranteModelAssembler.toCollectionDTO(restaurantes));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/por-nome-taxa-criteria-api")
	public ResponseEntity<List<RestauranteDTO>> listaRetaurantePorNomeTaxaCriteriaApi(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		List<Restaurante> restaurantes = restauranteRepository.findCriteriaQuery(nome, taxaFreteInicial, taxaFreteFinal);
		
		if (restaurantes.size() > 0) {
			return ResponseEntity.ok(restauranteModelAssembler.toCollectionDTO(restaurantes));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/frete-gratis")
	public ResponseEntity<List<RestauranteDTO>> listarRestauranteComFreteGratis(@RequestParam String nome) {
		Specification<Restaurante> comFreteGratis = new RestauranteComFreteGratisSpec();
		Specification<Restaurante> comNomeSemelhante = new RestauranteComNomeSemelhante(nome);
		
		List<Restaurante> restaurantes = restauranteRepository.findAll(comFreteGratis.and(comNomeSemelhante));
				
		if (restaurantes.size() > 0) {
			return ResponseEntity.ok(restauranteModelAssembler.toCollectionDTO(restaurantes));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/frete-gratis-fabrica")
	public ResponseEntity<List<RestauranteDTO>> listarRestauranteComFreteGratisFabrica(@RequestParam String nome) {
		List<Restaurante> restaurantes = restauranteRepository.findRestauranteComFreteGratis(nome);
		
		if (restaurantes.size() > 0) {
			return ResponseEntity.ok(restauranteModelAssembler.toCollectionDTO(restaurantes));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/primeiro")
	public ResponseEntity<RestauranteDTO> buscarRestaurantePrimeiro() {
		Optional<Restaurante> restauranteOptional = restauranteRepository.buscarPrimeiro();
		
		if (restauranteOptional.isPresent()) {
			Restaurante restaurante = restauranteOptional.get();
			return ResponseEntity.ok(restauranteModelAssembler.toDTO(restaurante));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteDTO adicionar(@RequestBody @Valid RestauranteInputDTO restauranteInput) {
		Restaurante restaurante = restauranteInputDisassembler.toDomainRestaurante(restauranteInput);
		
		try {
			return restauranteModelAssembler.toDTO(restauranteCadastro.salvar(restaurante));			
		} catch (CozinhaNaoEncontradaExecption | CidadeNaoEncontradaException e) {
			throw new NegocioExecption(e.getMessage(), e);
		}
	}

	@PutMapping("/{restauranteId}")
	public RestauranteDTO atualizar(@PathVariable Long restauranteId, @RequestBody @Valid RestauranteInputDTO restauranteInput) {
		Restaurante restauranteAtual = restauranteCadastro.buscarOuFalhar(restauranteId);
		restauranteAtual.setCozinha(new Cozinha());
		restauranteAtual.setEndereco(new Endereco());
		
//		Restaurante restaurante = restauranteInputDisassembler.toDomainRestaurante(restauranteInput);
		
//		BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
		
		restauranteInputDisassembler.copyToDomainRestaurante(restauranteInput, restauranteAtual);

		try {
			return restauranteModelAssembler.toDTO(restauranteCadastro.salvar(restauranteAtual));			
		} catch (CozinhaNaoEncontradaExecption | CidadeNaoEncontradaException e) {
			throw new NegocioExecption(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/{restauranteId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long restauranteId) {
		restauranteCadastro.excluir(restauranteId);
	}
	
	@PatchMapping("/{restauranteId}")
	public RestauranteDTO atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos, HttpServletRequest request) {		
		Restaurante restauranteAtual = restauranteCadastro.buscarOuFalhar(restauranteId);
		
		merge(campos, restauranteAtual, request);
		
		validate(restauranteAtual, "restaurante");
		
		RestauranteInputDTO restauranteInput = restauranteModelAssembler.toRestauranteDTO(restauranteAtual);
		
		return atualizar(restauranteId, restauranteInput);
	}

	private void validate(Restaurante restaurante, String objectName) {
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
		
		validator.validate(restaurante, bindingResult);
		
		if (bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
	}
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		restauranteCadastro.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		restauranteCadastro.inativar(restauranteId);
	}
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativacoes(@RequestBody List<Long> restaurantesId) {
		restauranteCadastro.ativacoes(restaurantesId);
	}
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desativacoes(@RequestBody List<Long> restaurantesId) {
		restauranteCadastro.desativacoes(restaurantesId);
	}
	
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		restauranteCadastro.fechar(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
		restauranteCadastro.abrir(restauranteId);
	}

	/**
	 * Atualização parcial com a API de Reflections do Spring
	 * 
	 * @param dadosOrigem
	 */
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		// Manipulação manual do objeto com jackson
		ObjectMapper objectMapper = new ObjectMapper();
		
		// Habilitando erros na desserializacao de propriedades ignoradas
		objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
		// Habilitando erros na desserializacao de propriedades inexistentes
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		
		try {
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);			
			
			dadosOrigem.forEach(( nomePropriedade, valorPropriedade ) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				
				Object novoObjeto = ReflectionUtils.getField(field, restauranteOrigem);
				
				ReflectionUtils.setField(field, restauranteDestino, novoObjeto);
			});
		} catch (IllegalArgumentException ex) {
			// Recupera a causa raiz utilizando ExceptionUtils da apache.commons.lang3
			Throwable rootCause = ExceptionUtils.getRootCause(ex);
			
			throw new HttpMessageNotReadableException(ex.getMessage(), rootCause, serverHttpRequest);
		}
		
	}
	

	

	
	
}
