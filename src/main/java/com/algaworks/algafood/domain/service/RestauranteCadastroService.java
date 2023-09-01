package com.algaworks.algafood.domain.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioExecption;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoExecption;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteCadastroService {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaCadastroService cozinhaCadastro;
	
	@Autowired
	private CidadeCadastroService cidadeCadastro;
	
	@Autowired
	private UsuarioCadastroService usuarioCadastro;
	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaCadastro.buscarOuFalhar(cozinhaId);
		restaurante.setCozinha(cozinha);
		
		Long cidadeId = restaurante.getEndereco().getCidade().getId();
		Cidade cidade = cidadeCadastro.buscarOuFalhar(cidadeId);  
		restaurante.getEndereco().setCidade(cidade); 			

		return restauranteRepository.save(restaurante);
	}
	
	@Transactional
	public void excluir(Long restauranteId) {
		restauranteRepository.deleteById(restauranteId);
		restauranteRepository.flush();
		
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId).orElseThrow(() -> new RestauranteNaoEncontradoExecption(restauranteId));
	}
	
	@Transactional
	public void ativar(Long restauranteId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		restaurante.ativar();
	}
	
	@Transactional
	public void inativar(Long restauranteId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		restaurante.inativar();
	}
	
	@Transactional
	public void ativacoes(List<Long> restaurantesId) {
		try {			
			restaurantesId.forEach(this::ativar);
		} catch (RestauranteNaoEncontradoExecption e) {
			throw new NegocioExecption(e.getMessage(), e);
		}
	}
	
	@Transactional
	public void desativacoes(List<Long> restaurantesId) {
		try {			
			restaurantesId.forEach(this::inativar);
		} catch (RestauranteNaoEncontradoExecption e) {
			throw new NegocioExecption(e.getMessage(), e);
		}
	}
	
	@Transactional
	public void fechar(Long restaurantId) {
		Restaurante restaurante = buscarOuFalhar(restaurantId);
		restaurante.fechar();
	}
	
	@Transactional
	public void abrir(Long restaurantId) {
		Restaurante restaurante = buscarOuFalhar(restaurantId);
		restaurante.abrir();
	}
	
	@Transactional
	public void desassociarFormaPagamento(Restaurante restaurante, FormaPagamento formaPagamento) {
		Collection<FormaPagamento> formasPagamento = restaurante.getFormasPagamento();
		formasPagamento.remove(formaPagamento);
	}
	
	@Transactional
	public void associarFormaPagamento(Restaurante restaurante, FormaPagamento formaPagamento) {
		Collection<FormaPagamento> formasPagamento = restaurante.getFormasPagamento();
		formasPagamento.add(formaPagamento);
	}
	
	@Transactional
	public void associarResponsavel(Long restauranteId, Long usuarioId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		Usuario usuario = usuarioCadastro.buscarOuFalhar(usuarioId);
		
		restaurante.adicionarResponsavel(usuario);
	}
	
	@Transactional
	public void desassociarResponsavel(Long restauranteId, Long usuarioId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		Usuario usuario = usuarioCadastro.buscarOuFalhar(usuarioId);
		
		restaurante.removerResponsavel(usuario);
	}

}
