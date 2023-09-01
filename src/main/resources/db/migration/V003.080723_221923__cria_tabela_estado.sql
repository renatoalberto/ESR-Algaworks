CREATE TABLE estado (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(80) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 
alter table cidade add column estado_id bigint not null;

update cidade c
  set c.estado_id = (select e.id from estado e where e.nome = c.nome_estado);

alter table cidade add constraint fk_cidade_estado 
foreign key (estado_id) references estado (id);

alter table cidade drop column nome_estado;

alter table cidade change column nome_cidade nome varchar(80) not null;