insert into clientes(id, nombre, apellido, email, create_at, foto) values(1,'miguel','gomez','miguel.gomez@aa.com', '2019-11-28', '');
insert into clientes(id, nombre, apellido, email, create_at, foto) values(2,'andres','guzman','andres.guzman@gmail.com','2019-11-27', '');

insert into productos(nombre, precio, create_at) values('Aire Acondicionado Split Frío Calor 3300 W', 26499, NOW());
insert into productos(nombre, precio, create_at) values('Smart Tv 43" Full Hd 43PFG5813/77', 20999, NOW());
insert into productos(nombre, precio, create_at) values('Smart Tv 50" 4K Ultra HD L50P8M Con Android TCL', 41999, NOW());
insert into productos(nombre, precio, create_at) values('Celular Libre A50 6,4" 64 GB Azul SAMSUNG', 27999, NOW());
insert into productos(nombre, precio, create_at) values('Heladera Con Freezer Marshall nueva linea MTIR3 2500 250 L', 23499, NOW());
insert into productos(nombre, precio, create_at) values('Celular Libre A50 6,4" 64 GB Azul SAMSUNG', 27999, NOW());
insert into productos(nombre, precio, create_at) values('Aire Acondicionado Split Frío Solo 2600 W PHS25CA1AN PHILCO', 24999, NOW());

insert into facturas(descripcion, cliente_id, create_at) values('Equipamento Electronico 1', 1, NOW());
insert into facturas_items(cantidad, factura_id, producto_id) values(1, 1, 1);
insert into facturas_items(cantidad, factura_id, producto_id) values(2, 1, 2);
insert into facturas_items(cantidad, factura_id, producto_id) values(1, 1, 3);
insert into facturas_items(cantidad, factura_id, producto_id) values(3, 1, 4);

insert into facturas(descripcion, cliente_id, create_at) values('Equipamento Electronico 2', 2, NOW());
insert into facturas_items(cantidad, factura_id, producto_id) values(1, 2, 5);
insert into facturas_items(cantidad, factura_id, producto_id) values(2, 2, 6);
insert into facturas_items(cantidad, factura_id, producto_id) values(1, 2, 7);


