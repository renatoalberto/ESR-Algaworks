package com.algaworks.algafood.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioExecption;
import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.PedidoCadastroService;
import com.algaworks.algafood.infrastructure.repository.especification.PedidoSpecs;

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
	
//	@GetMapping
//	public MappingJacksonValue pesquisar(PedidoFilter filtro, @RequestParam(required = false) String campos) {
//		List<Pedido> pedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro));
//		List<PedidoResumoDTO> pedidosDTO = pedidoResumoModelAssembler.toCollectionPedidoResumoDTO(pedidos);
//		
//		MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosDTO);
//		
//		SimpleFilterProvider filterProvider = new SimpleFilterProvider();                      // Limitando os campos retornados pela API com @JsonFilter do Jackson
//		filterProvider.addFilter("pedidoFiltro", SimpleBeanPropertyFilter.serializeAll());
//		
//		if (StringUtils.isNotBlank(campos)) {
//			filterProvider.addFilter("pedidoFiltro", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
//		}
//		
//		pedidosWrapper.setFilters(filterProvider);
//		
//		return pedidosWrapper;
//	}
//	
	@GetMapping
	public Page<PedidoResumoDTO> pesquisar(Pageable pageable, @PageableDefault(size = 10) PedidoFilter filtro) {
		pageable = traduzirPageable(pageable);
		
		Page<Pedido> pedidosPage = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), pageable);
		List<PedidoResumoDTO> pedidosDTO = pedidoResumoModelAssembler.toCollectionPedidoResumoDTO(pedidosPage.getContent());		
		Page<PedidoResumoDTO> pedidoDTOPage = new PageImpl<>(pedidosDTO, pageable, pedidosPage.getSize());
		
		return pedidoDTOPage;
	}
	
	private Pageable traduzirPageable(Pageable pageable) {
		Map<String, String> mapeamento = new HashMap<>();
		mapeamento.put("codigo", "codigo");
		mapeamento.put("subtotal", "subtotal");
		mapeamento.put("taxaFrete", "taxaFrete");
		mapeamento.put("valortotal", "valorTotal");
		mapeamento.put("statusPedido", "statusPedido");
		mapeamento.put("dataCriacao", "dataCriacao");
		mapeamento.put("dataConfirmacao", "dataConfirmacao");
		mapeamento.put("dataCancelamento", "dataCancelamento");
		mapeamento.put("dataEntrega", "dataEntrega");
		mapeamento.put("restaurante.id", "restaurante.id");
		mapeamento.put("restaurante.nome", "restaurante.nome");
		mapeamento.put("cliente.id", "cliente.id");
		mapeamento.put("cliente.nome", "cliente.nome");  
		mapeamento.put("cliente.email", "cliente.email");  
		mapeamento.put("cliente.dataCadastro", "cliente.dataCadastro");  
		mapeamento.put("nomeCliente", "cliente.nome");                   // exemplo de nome que não corresponde com a entidade
		
		return PageableTranslator.tradutorPage(pageable, mapeamento);
	}

	@GetMapping("/{pedidoCodigo}")
	public PedidoDTO buscar(@PathVariable String pedidoCodigo) {
		Pedido pedido = pedidoCadastro.buscarOuFalhar(pedidoCodigo);
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
