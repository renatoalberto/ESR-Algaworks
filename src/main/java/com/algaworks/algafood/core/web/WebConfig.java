package com.algaworks.algafood.core.web;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 * 16.6. Habilitando CORS globalmente no projeto da API
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")                                 // Padrão para permitir qualquer origem
//			.allowedOrigins("http://www.algafood.local:8000/")     // Para permitir origem específica
			.allowedMethods("GET", "HEAD", "POST", "DELETE");      // Padrão para métodos "GET", "HEAD", "POST"
//			.maxAge(30);                                           // Tempo de cache de 30 segundos
	}

	// 17.5. Implementando requisições condicionais com Shallow ETags
    @Bean
    Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}

}
