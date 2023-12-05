function consultarCidades() {
  $.ajax({
    url: "http://api.algafood.local:8080/cidades",
    type: "get",
    headers: {"S-teste": "abc"},

    success: function(response) {
      $("#conteudo").text(JSON.stringify(response));
    }
  });
}

$("#botao").click(consultarCidades);