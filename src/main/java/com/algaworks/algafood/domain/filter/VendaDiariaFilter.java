package com.algaworks.algafood.domain.filter;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaDiariaFilter {
	
	private Long restauranteId;
	
	@DateTimeFormat(iso = ISO.DATE)
	private Date dataCriacaoInicio;
	
	@DateTimeFormat(iso = ISO.DATE)
	private Date dataCriacaoFim;

}
