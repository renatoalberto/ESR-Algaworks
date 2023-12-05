package com.algaworks.algafood.core.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.domain.service.EnvioEmailService;
import com.algaworks.algafood.infrastructure.repository.email.FakeEnvioEmailService;
import com.algaworks.algafood.infrastructure.repository.email.SandboxEnvioEmailService;
import com.algaworks.algafood.infrastructure.repository.email.SmtpEnvioEmailService;

@Configuration
public class EmailConfig {
	
	@Autowired
	private EmailProperties emailProperties;

    @Bean
    EnvioEmailService envioEmailService() {
		
		switch (emailProperties.getImpl()) {
			case SMTP:    
				return new SmtpEnvioEmailService();
			case SANDBOX: 
				return new SandboxEnvioEmailService();
			case FAKE:    
				return new FakeEnvioEmailService();
			default:      
				return null;
		}
		
	}

}
