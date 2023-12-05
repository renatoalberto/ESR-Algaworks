package com.algaworks.algafood.infrastructure.repository.email;

import lombok.extern.slf4j.Slf4j;

/***
 * 15.8. Desafio: implementando serviço de envio de e-mail fake
 */
@Slf4j
//@Service
public class FakeEnvioEmailService extends SmtpEnvioEmailService {
	
	@Override
	public void enviar(Mensagem mensagem) {
		
		String corpo = processarTemplate(mensagem);
		
		log.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), corpo);
		
	}
	
}
