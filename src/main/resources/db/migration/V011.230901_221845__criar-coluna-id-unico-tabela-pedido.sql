alter table pedido add id_unico varchar(36) not null after id;

alter table pedido add constraint uk_pedido_id_unico unique (id_unico);