package com.algaworks.algafood.infrastructure.repository.email;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.algaworks.algafood.core.email.EmailProperties;

/***
 * 15.9. Desafio: Implementando serviço de envio de e-mail sandbox
 */
public class SandboxEnvioEmailService extends SmtpEnvioEmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailProperties emailProperties;
	
	@Override
	public void enviar(Mensagem mensagem) {
		try {
			String corpo = processarTemplate(mensagem);

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(emailProperties.getRemetente());
			helper.setTo(emailProperties.getSandbox().getDestinatario());
			helper.setSubject(mensagem.getAssunto());
			helper.setText(corpo, true);   // true informa que é email é em html
			
			mailSender.send(mimeMessage);
			
		} catch (Exception e) {
			throw new EmailException("Não foi possível enviar email", e);
		}
		
	}

}
