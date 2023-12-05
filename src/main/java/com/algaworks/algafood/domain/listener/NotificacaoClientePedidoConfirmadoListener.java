package com.algaworks.algafood.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import com.algaworks.algafood.domain.service.EnvioEmailService.Mensagem;

@Component
public class NotificacaoClientePedidoConfirmadoListener {
	
	@Autowired
	private EnvioEmailService envioEmail;	
	
//	@EventListener                 // evento inicia antes do commit
//	@TransactionalEventListener    // evento inicia após commit
	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)  // Fica mais claro o momento de início do evento
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		Pedido pedido = event.getPedido();
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
//		        .corpo("O pedido de código <strong>" + pedido.getCodigo() + "</strong> foi confirmado!")
		        .corpo("pedido-confirmado.html")
		        .variavel("pedido", pedido)
		        .destinatario(pedido.getCliente().getEmail())
//		        .destinatario("email.fake@algafood.com.lixo")         // exemplo apresenta gravar varios emails
		        .build();

         envioEmail.enviar(mensagem);		
		
	}

}
