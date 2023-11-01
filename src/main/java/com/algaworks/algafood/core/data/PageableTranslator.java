package com.algaworks.algafood.core.data;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableTranslator {
	
	// Método converte o nome do parametro informado para sort, em um parametros valido da entidade
	public static Pageable tradutorPage(Pageable pegeable, Map<String, String> camposSort) {
		var orders = pegeable.getSort().stream()
			.filter(order -> camposSort.containsKey(order.getProperty()))
			.map(order -> new Sort.Order(order.getDirection(), camposSort.get(order.getProperty())))
			.collect(Collectors.toList());
		
		return PageRequest.of(pegeable.getPageNumber(), pegeable.getPageSize(), Sort.by(orders));
	}
	

}
