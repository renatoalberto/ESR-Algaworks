package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.api.model.input.CozinhaInputDTO;
import com.algaworks.algafood.api.model.output.CozinhaDTO;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CozinhaCadastroService;

@RestController
@RequestMapping(value = "/cozinhas")  // , produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class CozinhaController {
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CozinhaCadastroService cozinhaCadastro;
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;
	
	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CozinhaDTO> listarJson() {
		return cozinhaModelAssembler.toCollectionDTO(cozinhaRepository.findAll());
	}
	
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhasXmlWrapper listarXml() {
		return new CozinhasXmlWrapper(cozinhaModelAssembler.toCollectionDTO(cozinhaRepository.findAll()));
	}
	
	@GetMapping("/por-nome")
	public ResponseEntity<List<CozinhaDTO>> listarPorNome(@RequestParam("nome") String nome) {
		Optional<List<Cozinha>> cozinhas = cozinhaRepository.findTodasByNome(nome);
		
		if (cozinhas.get().size() > 0) {
			return ResponseEntity.ok(cozinhaModelAssembler.toCollectionDTO(cozinhas.get()));					
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/por-parte-nome")
	public ResponseEntity<List<CozinhaDTO>> listarPorParteNome(@RequestParam("nome") String nome) {
		Optional<List<Cozinha>> cozinhas = cozinhaRepository.findTodasByNomeContaining(nome);
		
		System.out.println(cozinhas.get());
		
		if (cozinhas.get().size() > 0) {
			return ResponseEntity.ok(cozinhaModelAssembler.toCollectionDTO(cozinhas.get()));					
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@GetMapping("/primeiro-nome")
	public ResponseEntity<CozinhaDTO> recuperaPrimeiroPorParteNome(@RequestParam("nome") String nome) {
		Optional<Cozinha> cozinha = cozinhaRepository.findFirstByNomeContaining(nome);
		
		if (cozinha.isPresent()) {
			return ResponseEntity.ok(cozinhaModelAssembler.toDTO(cozinha.get()));					
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@GetMapping("/2-primeiros-nome")
	public ResponseEntity<List<CozinhaDTO>> recupera2PrimeirosPorParteNome(@RequestParam("nome") String nome) {
		Optional<List<Cozinha>> cozinhas = cozinhaRepository.findTop2ByNomeContaining(nome);
		
		if (cozinhas.get().size() > 0) {
			return ResponseEntity.ok(cozinhaModelAssembler.toCollectionDTO(cozinhas.get()));					
		}
		
		return ResponseEntity.notFound().build();		
	}
	
	@GetMapping("/existe-nome")
	public boolean existePorParteNome(@RequestParam("nome") String nome) {
		return cozinhaRepository.existsByNomeContaining(nome);		
	}
	
	@GetMapping("/contador-nome")
	public int contadorPorParteNome(@RequestParam("nome") String nome) {
		return cozinhaRepository.countByNomeContaining(nome);		
	}
	
//	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
//	public List<Cozinha> listarXml() {
//		System.out.println("listarXml() acionado");
//		return cozinhaRepository.listar();
//	}
	
//	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{cozinhaId}")
	public CozinhaDTO buscar(@PathVariable("cozinhaId") Long id) {
		return cozinhaModelAssembler.toDTO(cozinhaCadastro.buscarOuFalhar(id));
		
//		HttpHeaders header = new HttpHeaders();
//		header.add(HttpHeaders.LOCATION, "http://api.algafood.local:8080/cozinhas");
//		
//		return ResponseEntity.status(HttpStatus.FOUND)
//				.headers(header)
//				.build();
	}
	
	@GetMapping("/primeiro")
	public ResponseEntity<CozinhaDTO> buscarCozinhaPrimeiro() {
		Optional<Cozinha> cozinha = cozinhaRepository.buscarPrimeiro();
		
		if (cozinha.isPresent()) {
			return ResponseEntity.ok(cozinhaModelAssembler.toDTO(cozinha.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<CozinhaDTO> adicionar(@RequestBody @Valid CozinhaInputDTO cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainCozinha(cozinhaInput);
		CozinhaDTO cozinhaDTO = cozinhaModelAssembler.toDTO(cozinhaCadastro.salvar(cozinha));
		return ResponseEntity.status(HttpStatus.CREATED).body(cozinhaDTO);
	}
	
	@PutMapping("/{cozinhaId}")
	public CozinhaDTO atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInputDTO cozinhaInput) {
		Cozinha cozinhaAtual = cozinhaCadastro.buscarOuFalhar(cozinhaId);
	
//		BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");          // copia origem para destino, ignorando o id
		cozinhaInputDisassembler.copyToCozinhaModel(cozinhaInput, cozinhaAtual);
		
		return cozinhaModelAssembler.toDTO(cozinhaCadastro.salvar(cozinhaAtual));
	}
	
// Tratando excessões de forma individual
//	@DeleteMapping("/{cozinhaId}")
//	public ResponseEntity<?> excluir(@PathVariable Long cozinhaId) {
//		try {
//			cozinhaCadastro.excluir(cozinhaId);
//			return ResponseEntity.noContent().build();
//			
//		} catch (CozinhaNaoEncontradaExecption e) {
//			return ResponseEntity.notFound().build();
//			
//		} catch (EntidadeEmUsoException e) {                        // Tratamento para restrição de fk
//			return ResponseEntity.status(HttpStatus.CONFLICT).build();
//		}
//	}
	
// Excessões tratadas pelas proprias exceptions com @ResponseStatus
//	@DeleteMapping("/{cozinhaId}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void excluir(@PathVariable Long cozinhaId) {
//		cozinhaCadastro.excluir(cozinhaId);
//	}
	
// Excessões tratadas com ResponseStatusException de forma genérica do Spring 
// Usar em projeto mais simples, ou quando não quer gastar tempo criando exceptions personalisadas	
//	@DeleteMapping("/{cozinhaId}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void excluir(@PathVariable Long cozinhaId) {
//		try {
//			cozinhaCadastro.excluir(cozinhaId);			
//		} catch (CozinhaNaoEncontradaExecption e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());			
//		}
//	}
	
// Excessões tratadas com ResponseStatusException extendida na EntidadeNaoEncontradaExecption
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long cozinhaId) {
		cozinhaCadastro.excluir(cozinhaId);			
	}
	
	

}
