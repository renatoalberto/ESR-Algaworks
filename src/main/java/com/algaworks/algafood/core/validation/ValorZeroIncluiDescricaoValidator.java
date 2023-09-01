package com.algaworks.algafood.core.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;

public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {
	
	private String valorField;
	private String descricaoField;
	private String decricaoObrigatoria;
	
	@Override
	public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
		this.valorField = constraintAnnotation.valorField();
		this.descricaoField = constraintAnnotation.descricaoField();
		this.decricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
	}
	
	@Override
	public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {
		boolean valido = true;
		
		try {
			BigDecimal valor = (BigDecimal) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), valorField).getReadMethod().invoke(objetoValidacao);
			String descricao = (String) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), descricaoField).getReadMethod().invoke(objetoValidacao);
			
			if (valor != null && descricao != null) {
				if (BigDecimal.ZERO.compareTo(valor) == 0) {
					valido = descricao.toLowerCase().contains(decricaoObrigatoria.toLowerCase());
				}
			}
			
			return valido;
		} catch (Exception e) {
			throw new ValidationException(e); 
		}
		
	}

}
