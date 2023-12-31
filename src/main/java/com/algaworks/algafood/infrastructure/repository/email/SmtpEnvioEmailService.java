package com.algaworks.algafood.infrastructure.repository.email;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.service.EnvioEmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/***
 * 15.3. Implementando o serviço de infraestrutura de envio de e-mails com Spring
 */
//@Service
public class SmtpEnvioEmailService implements EnvioEmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailProperties emailProperties;
	
	@Autowired
	private Configuration freemarkerConfig;

	@Override
	public void enviar(Mensagem mensagem) {
		try {
			String corpo = processarTemplate(mensagem);

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(emailProperties.getRemetente());
			helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
			helper.setSubject(mensagem.getAssunto());
			helper.setText(corpo, true);   // true informa que é email é em html
			
			mailSender.send(mimeMessage);
			
		} catch (Exception e) {
			throw new EmailException("Não foi possível enviar email", e);
		}
		
	}
	
	protected String processarTemplate(Mensagem mensagem) {
		try {
			Template template =  freemarkerConfig.getTemplate(mensagem.getCorpo());
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, mensagem.getVariaveis());
		} catch (Exception e) {
			throw new EmailException("Não foi possível montar o template do email", e);
		}
	}

}
