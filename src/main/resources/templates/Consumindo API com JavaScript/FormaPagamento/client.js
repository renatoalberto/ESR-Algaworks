function consultar() {
  $.ajax({
    url: "http://api.algafood.local:8080/forma-pagamento",
    type: "get",

    success: function(response) {
      preencherTabela(response);
    }
  });
}

function cadastrar() {
	var formaPagamento = JSON.stringify({
		  "descricao": $("#campo-descricao").val()
		});
		
    console.log(formaPagamento);
		
	$.ajax({
		url: "http://api.algafood.local:8080/forma-pagamento",
		type: "post",
		data: formaPagamento,
		contentType: "application/json",
		
		success: function(response) {
			alert("Forma de pagamento cadastrada");
			consultar();
		},
		
		error: function(error) {
			if (error.status == 400) {
				var problem = JSON.parse(error.responseText);
				alert(problem.userMessager);
			} else {
				alert("Erro ao cadastrar forma de pagamento");
			}
		}
		
	});
}


function preencherTabela(formasPagamento) {
  $("#tabela tbody tr").remove();

  $.each(formasPagamento, function(i, formaPagamento) {
    var linha = $("<tr>");
    
    var linkAcao = $("<a href='#'>")
      .text("Excluir")
      .click(function(event) {
        event.preventDefault();
        excluir(formaPagamento);
      });

    linha.append(
      $("<td>").text(formaPagamento.id),
      $("<td>").text(formaPagamento.descricao),
      $("<td>").append(linkAcao)      
    );

    linha.appendTo("#tabela");
  });
}

function excluir(formaPagamento) {
  $.ajax({
    url: "http://api.algafood.local:8080/forma-pagamento/" + formaPagamento.id,
    type: "delete",

    success: function(response) {
		alert("Forma de pagamento " + formaPagamento.id + " excluida.");
		consultar();
    },
    
	error: function(error) {
      // tratando todos os erros da categoria 4xx
      if (error.status >= 400 && error.status <= 499) {
        var problem = JSON.parse(error.responseText);
          alert(problem.userMessager);
      } else {
          alert("Erro ao remover forma de pagamento");
      }}    
  });	
}


$("#btn-consultar").click(consultar);
$("#btn-cadastrar").click(cadastrar);