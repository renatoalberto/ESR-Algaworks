package com.algaworks.algafood.core.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultiploValidator implements ConstraintValidator<Multiplo, Number>{
	
	private int numeroMultiplo;

	@Override
	public void initialize(Multiplo constraintAnnotation) {
		this.numeroMultiplo = constraintAnnotation.numero();
	}
	
	@Override
	public boolean isValid(Number value, ConstraintValidatorContext context) {
		boolean valido = true;
		
		if (value != null) {
			BigDecimal numeroBigDecimal = BigDecimal.valueOf(value.doubleValue());
			BigDecimal multiploBigDecimal = BigDecimal.valueOf(numeroMultiplo);
			BigDecimal resto = numeroBigDecimal.remainder(multiploBigDecimal);
			valido = BigDecimal.ZERO.compareTo(resto) == 0;
		}
		
		return valido;
	}
	
	

}
