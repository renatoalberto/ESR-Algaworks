CREATE TABLE pedido (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sub_total` decimal(10,2) not null,
  `taxa_frete` decimal(10,2) not null,
  `valor_total` decimal(10,2) not null,
  `data_criacao` datetime not null,
  `data_confirmacao` datetime,
  `data_cancelamento` datetime,
  `data_entrega` datetime,
  `status` varchar(10) not null,
  
  `restaurante_id` bigint not null,
  `usuario_cliente_id` bigint not null,
  `forma_pagamento_id` bigint not null,

  `endereco_cidade_id` bigint(20) not null,   
  `endereco_bairro` varchar(80) not null, 
  `endereco_cep` varchar(80) not null, 
  `endereco_complemento` varchar(80), 
  `endereco_logradouro` varchar(80) not null, 
  `endereco_numero` varchar(80) not null, 
  
  PRIMARY KEY (`id`),
  
  constraint fk_pedido_endereco_cidade foreign key (endereco_cidade_id) references cidade (id),
  constraint fk_pedido_restaurante foreign key (restaurante_id) references restaurante (id),
  constraint fk_pedido_usuario_cliente foreign key (usuario_cliente_id) references usuario (id),
  constraint fk_pedido_forma_pagamento foreign key (forma_pagamento_id) references forma_pagamento (id)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE item_pedido (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantidade` smallint(6) not null,
  `preco_unitario` decimal(10,2) not null,
  `preco_total` decimal(10,2) not null,
  `observacao` varchar(255),
  
  `pedido_id` bigint not null,
  `produto_id` bigint not null,
  
  primary key (`id`),
  
  unique key uk_item_pedido_produto (pedido_id, produto_id),
  
  constraint fk_item_pedido_pedido foreign key (pedido_id) references pedido (id),
  constraint fk_item_pedido_produto foreign key (produto_id) references produto (id)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

