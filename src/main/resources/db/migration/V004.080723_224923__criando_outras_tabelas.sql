create table forma_pagamento (
	id bigint not null auto_increment, 
	descricao varchar(60) not null, 
	primary key (id)
) engine=InnoDB;

create table grupo (
	id bigint not null auto_increment, 
    nome varchar(80) not null, 
    primary key (id)
) engine=InnoDB;

create table grupo_permissao (
	grupo_id bigint not null, 
    permissao_id bigint not null
) engine=InnoDB;

create table permissao (
	id bigint not null auto_increment, 
    descricao varchar(80) not null, 
    nome varchar(80) not null, 
    primary key (id)
) engine=InnoDB;

create table produto (
	id bigint not null auto_increment, 
    ativo bit not null, 
    descricao varchar(200) not null, 
    nome varchar(80) not null, 
    preco decimal(19,2) not null, 
    restaurante_id bigint not null, 
    primary key (id)
) engine=InnoDB;

create table restaurante (
	id bigint not null auto_increment, 
    data_atualizacao datetime(6) not null, 
    data_cadastro datetime(6) not null, 
    endereco_bairro varchar(80), 
    endereco_cep varchar(80), 
    endereco_complemento varchar(80), 
    endereco_logradouro varchar(80), 
    endereco_numero varchar(80), 
    nome varchar(80) not null, 
    taxa_frete decimal(19,2) not null, 
    cozinha_id bigint not null, 
    endereco_cidade_id bigint, 
    primary key (id)
) engine=InnoDB;

create table restaurante_formas_pagamento (
	restaurante_id bigint not null, 
	forma_pagamento_id bigint not null
) engine=InnoDB;

create table usuario (
	id bigint not null auto_increment, 
    data_cadastro datetime(6) not null, 
    email varchar(80) not null, 
    nome varchar(80) not null, 
    senha varchar(80) not null, 
    primary key (id)
) engine=InnoDB;

create table usuario_grupo (
	usuario_id bigint not null, 
    grupo_id bigint not null
) engine=InnoDB;

alter table grupo_permissao add constraint fk_grupo_permissao_permissao 
foreign key (permissao_id) references permissao (id);

alter table grupo_permissao add constraint fk_grupo_permissao_grupo 
foreign key (grupo_id) references grupo (id);

alter table produto add constraint fk_produto_restaurante 
foreign key (restaurante_id) references restaurante (id);

alter table restaurante add constraint fk_restaurante_cozinha 
foreign key (cozinha_id) references tab_cozinhas (id);

alter table restaurante add constraint fk_restaurante_cidade 
foreign key (endereco_cidade_id) references cidade (id);

alter table restaurante_formas_pagamento add constraint fk_restaurante_formas_pagamento_forma_pagamento 
foreign key (forma_pagamento_id) references forma_pagamento (id);

alter table restaurante_formas_pagamento add constraint fk_restaurante_formas_pagamento_restaurante 
foreign key (restaurante_id) references restaurante (id);

alter table usuario_grupo add constraint fk_usuario_grupo_grupo 
foreign key (grupo_id) references grupo (id);

alter table usuario_grupo add constraint fk_usuario_grupo_usuario 
foreign key (usuario_id) references usuario (id);