package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;

@Service
public class PedidoCadastroService {
	
	private static final String MSG_PEDIDO_EM_USO = "Pedido de código %d não pode ser removido por estar em uso";
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private FormaPagamentoCadastroService formaPagamentoCadastro;
	
	@Autowired
	private RestauranteCadastroService restauranteCadastro;
	
	@Autowired
	private CidadeCadastroService cidadeCadastro;
	
	@Autowired
	private UsuarioCadastroService usuarioCadastro;
	
	@Autowired
	private ProdutoCadastroService produtoCadastro;
	
	public Pedido buscarOuFalhar(Long pedidoId) {
		return pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
	}
	
	@Transactional
	public void excluir(Long pedidoId) {
		try {
			pedidoRepository.deleteById(pedidoId);			
		} catch (EmptyResultDataAccessException e) {
			throw new PedidoNaoEncontradoException(pedidoId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_PEDIDO_EM_USO, pedidoId));
		} catch (RuntimeException e) {
			System.out.println(e.getClass() + " " + e.getMessage());
		}
	}
	
	@Transactional
	public Pedido salvar(Pedido pedido) {
		Long formaPagamentoId = pedido.getFormaPagamento().getId();
		FormaPagamento formaPagamento = formaPagamentoCadastro.buscarOuFalhar(formaPagamentoId);
		
		Long restauranteId = pedido.getRestaurante().getId();
		Restaurante restaurante = restauranteCadastro.buscarOuFalhar(restauranteId);
		
		Long cidadeId = pedido.getEnderecoEntrega().getCidade().getId();
		Cidade cidade = cidadeCadastro.buscarOuFalhar(cidadeId);
		
		Long clienteId = pedido.getCliente().getId();
		Usuario cliente = usuarioCadastro.buscarOuFalhar(clienteId);
		
		
		if (!restaurante.getFormasPagamento().contains(formaPagamento)) {
			throw new FormaPagamentoNaoEncontradaException(restauranteId, formaPagamentoId);
		}
		
		pedido.getItens().forEach(item -> {
			Long produtosId = item.getProduto().getId();
			Produto produto = produtoCadastro.buscarOuFalhar(produtosId);
			
			if (produto.getRestaurante().getId() != restauranteId) {
				throw new ProdutoNaoEncontradoException(restaurante.getId(), produto.getId());
			}
			
			item.setProduto(produto);
			item.setPedido(pedido);		
			
			item.calculaValorTotal();
		});
			
		pedido.setFormaPagamento(formaPagamento);
		pedido.setRestaurante(restaurante);
		pedido.getEnderecoEntrega().setCidade(cidade);
		pedido.setCliente(cliente);
		pedido.setStatus(StatusPedido.CRIADO);
		
		pedido.setTaxaFrete(restaurante.getTaxaFrete());
		pedido.calculaSubTotal();
		pedido.calculaTotal();
		
		return pedidoRepository.save(pedido);
	}
	
}
