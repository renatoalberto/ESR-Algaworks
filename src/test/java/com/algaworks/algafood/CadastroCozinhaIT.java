package com.algaworks.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaExecption;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CozinhaCadastroService;

@SpringBootTest
class CadastroCozinhaIT {
	
	@Autowired
	CozinhaCadastroService cozinhaCadastro;
	
	@Autowired
	private RestauranteRepository restauranteRepository;

	@Test
	public void testarCadastroCozinhaComSucesso() {
		// cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		// ação
		novaCozinha = cozinhaCadastro.salvar(novaCozinha);
		
		// validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
		
		testarDelecaoDeCozinhaComSucesso(novaCozinha);
	}
	
	@Test
	public void testarCadastroCozinhaSemNomeComErro() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);
		
		ConstraintViolationException erroEsperado =
				Assertions.assertThrows(ConstraintViolationException.class, () -> {
					cozinhaCadastro.salvar(novaCozinha);
				});

		assertThat(erroEsperado).isNotNull();		
	}
	
	@Test
	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
		Optional<Restaurante> restaurante = restauranteRepository.buscarPrimeiro();
		
		if (restaurante.isPresent()) {
			EntidadeEmUsoException erroEsperado =
	        		Assertions.assertThrows(EntidadeEmUsoException.class,()->{
	        			cozinhaCadastro.excluir(restaurante.get().getCozinha().getId());
	        		});

			assertThat(erroEsperado).isNotNull();	
		}
		
	}
	
	@Test
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
		CozinhaNaoEncontradaExecption erroEsperado =
				Assertions.assertThrows(CozinhaNaoEncontradaExecption.class,()->{
					cozinhaCadastro.excluir(99999L);
				});
		
		assertThat(erroEsperado).isNotNull();	
		
	}
	
    public void testarDelecaoDeCozinhaComSucesso(Cozinha cozinha) {
        cozinhaCadastro.excluir(cozinha.getId());
        
        CozinhaNaoEncontradaExecption erroEsperado =
        		Assertions.assertThrows(CozinhaNaoEncontradaExecption.class,()->{
        			cozinhaCadastro.buscarOuFalhar(cozinha.getId());
        		});

		assertThat(erroEsperado).isNotNull();	
    }

}
