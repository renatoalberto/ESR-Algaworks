package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaExecption;
import com.algaworks.algafood.domain.exception.NegocioExecption;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice    // Componente com ExceptionHandler tratados de forma global
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {          // ResponseEntityExceptionHandler possui uma implementação padrão do spring para as exceptions do spring
	
	private static final String MSG_ERRO_INESPERADO_SISTEMA = "Ocorreu um erro interno inesperado no sistema. "
	        + "Tente novamente e se o problema persistir, entre em contato "
	        + "com o administrador do sistema.";
	
	@Autowired
	MessageSource messageSource;

	/**
	 * Quando o jackson não consegue converter o json para um objeto java corresponsente, retorna o erro HttpMessageNotReadableException
	 */
	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println(1);

		// Recupera a causa raiz utilizando ExceptionUtils da apache.commons.lang3
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		System.out.println("Exception causa raiz - " + rootCause.getClass().getSimpleName());
		
		if (rootCause instanceof InvalidFormatException) {
			return handlerInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlerPropertyBinding((PropertyBindingException) rootCause, headers, status, request); 
		}
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREESIVEL;
		String detail = "O corpor da requisição está inválido. Verifique o erro de sintaxe";
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessager(MSG_ERRO_INESPERADO_SISTEMA)
				.build();		
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	/**
	 * Tratamento de tipo inválido para o parametro da url
	 */
	@Override
	public ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println(2);
		
		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handlerMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		} 		
		
		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	@Override
	public ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println(3);
		
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADA;		
		
		String detail = String.format("O recurso '%s', que você tentou acessar, é inexistente.",
	            ex.getRequestURL()); 
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.build();		
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println(4);
		
		return handleValidationInternal(ex, ex.getBindingResult(), request);
	}
	
	private ResponseEntity<Object> handlerMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println(5);
		
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		
		String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
	            + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
	            ex.getName(), 
	            ex.getValue(), 
	            ex.getRequiredType().getSimpleName()); 
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessager(MSG_ERRO_INESPERADO_SISTEMA)
				.build();		
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);		
	}

	private ResponseEntity<Object> handlerInvalidFormat(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println(6);
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREESIVEL;
		
		String path = joinPath(ex.getPath());
		
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um valor compactível com o tipo %s.", 
				path, 
				ex.getValue(), 
				ex.getTargetType().getSimpleName());  
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessager(MSG_ERRO_INESPERADO_SISTEMA)
				.build();		
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);		
	}

	
	private ResponseEntity<Object> handlerPropertyBinding(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println(7);
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREESIVEL;
		
		String path = joinPath(ex.getPath());	
		
		String detail = String.format("A propriedade '%s' não é uma propriedade reconhecida. Corrija ou remova essa propriedade e tente novamente.", path);  
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessager(MSG_ERRO_INESPERADO_SISTEMA)
				.build();		
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	/**
	 * concatenar os nomes das propriedades (separando por ".")
	 * 
	 * @param references
	 * @return String
	 */
	private String joinPath(List<Reference> references) {
		System.out.println(8);
		
		String path = references.stream()
				.map(ref -> ref.getFieldName())
				.collect(Collectors.joining("."));
		
		return path;
	}

	@ExceptionHandler(EntidadeNaoEncontradaExecption.class)
	public ResponseEntity<?> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaExecption ex, WebRequest request) {
		System.out.println(9);
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADA;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.build();		
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
		
//		Problema problema = Problema.builder()
//				.datahora(OffsetDateTime.now())
//				.mensagem(e.getMessage())
//				.build();
//		
//		return ResponseEntity.status(HttpStatus.NOT_FOUND)
//				.body(problema);
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {
		System.out.println(10);
		
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.build();
				
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
		
//		Problema problema = Problema.builder()
//				.datahora(OffsetDateTime.now())
//				.mensagem(e.getMessage())
//				.build();
//		
//		return ResponseEntity.status(HttpStatus.CONFLICT)
//				.body(problema);
	}
	
	@ExceptionHandler({ ValidacaoException.class })
	public ResponseEntity<Object> handleValidacao(ValidacaoException ex, WebRequest request) {
		System.out.println(11);
		
	    return handleValidationInternal(ex, ex.getBindResult(), request);
	}     
	
	public ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, WebRequest request) {
		System.out.println(12);
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
		
		List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
				.map(objectError -> {
					String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());  // busca mensagens de validação globais em Resource Bundle - messages.properties			
					
					String name = objectError.getObjectName();
					
					if (objectError instanceof FieldError) {
						name = objectError.getObjectName() + "." + ((FieldError) objectError).getField();
					}
					
					return Problem.Object.builder()
						.name(name)
						.userMessage(message)              
						.build(); 
				})
				.collect(Collectors.toList());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessager(detail)
				.objects(problemObjects)
				.build();	
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}	
	
	@ExceptionHandler(NegocioExecption.class)
	public ResponseEntity<?> handleNegocio(NegocioExecption ex, WebRequest request) {
		System.out.println(13);
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessager(detail)
				.build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
		
//		Problema problema = Problema.builder()
//				.datahora(OffsetDateTime.now())
//				.mensagem(e.getMessage())
//				.build();
//		
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//				.body(problema);		
	}	

// Implementada pela classe estendida ResponseEntityExceptionHandler, comentada para evitar erro por ambiguidade
//	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//	public ResponseEntity<?> trataHttpMediaTypeNotSupportedException() {
//		Problema problema = Problema.builder()
//				.datahora(OffsetDateTime.now())
//				.mensagem("O tipo de midia não é aceito")
//				.build();
//		
//		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//				.body(problema);		
//	}
	
// handleExceptionInternal sobrescrito da classe Spring ResponseEntityExceptionHandler, para personalização do body
// método original retorna corpo (body) vazio	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		System.out.println(14);
		
		if (body == null) {
			System.out.println("14-1");
			body = Problem.builder()
					.status(status.value())
					.title(status.getReasonPhrase())
					.userMessager(MSG_ERRO_INESPERADO_SISTEMA)
					.timestamp(OffsetDateTime.now())
					.build();
		} else if (body instanceof String){
			System.out.println("14-2");
			body = Problem.builder()
					.status(status.value())
					.title((String) body)
					.userMessager(MSG_ERRO_INESPERADO_SISTEMA)
					.timestamp(OffsetDateTime.now())
					.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String details) {
		System.out.println(15);
		
		return Problem.builder()
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(details)
				.userMessager(details)
				.timestamp(OffsetDateTime.now());
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleErroSistema(Exception ex, WebRequest request) {
		System.out.println(16);
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		
		String detail = MSG_ERRO_INESPERADO_SISTEMA;
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessager(MSG_ERRO_INESPERADO_SISTEMA)
				.build();
		
	    // Importante colocar o printStackTrace para mostrar a stacktrace no console
	    // Se não fizer isso, você não vai ver a stacktrace de exceptions 
	    ex.printStackTrace();
				
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}	

}
