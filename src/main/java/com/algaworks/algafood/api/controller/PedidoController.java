package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelDTO;
import com.algaworks.algafood.api.model.input.PedidoInputDTO;
import com.algaworks.algafood.api.model.output.PedidoDTO;
import com.algaworks.algafood.api.model.output.PedidoResumoDTO;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioExecption;
import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.PedidoCadastroService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PedidoCadastroService pedidoCadastro;
	
	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;
	
	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;
	
	@Autowired
	private PedidoResumoModelDTO pedidoResumoModelAssembler;
	
	@GetMapping
	public List<PedidoResumoDTO> listar() {
		List<Pedido> pedidos = pedidoRepository.findAll();
		return pedidoResumoModelAssembler.toCollectionPedidoResumoDTO(pedidos);
	}
	
	@GetMapping("/{pedidoId}")
	public PedidoDTO listar(@PathVariable Long pedidoId) {
		Pedido pedido = pedidoCadastro.buscarOuFalhar(pedidoId);
		return pedidoModelAssembler.toPedidoDTO(pedido);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoDTO salvar(@RequestBody @Valid PedidoInputDTO pedidoInput) {
		Pedido pedido = pedidoInputDisassembler.toDomainPedido(pedidoInput);
		
		// será tratado pelo usuário logado
		Usuario cliente = new Usuario();
		cliente.setId(1L);
		pedido.setCliente(cliente);
		
		try {
			pedido = pedidoCadastro.salvar(pedido);			
		} catch (FormaPagamentoNaoEncontradaException | ProdutoNaoEncontradoException | CidadeNaoEncontradaException e) {
			throw new NegocioExecption(e.getMessage(), e);
		}
		
		return pedidoModelAssembler.toPedidoDTO(pedido);
	}

}
