
create table platos(
    nombre varchar(50) not null,
    precio decimal(5,2) not null,
    tipo enum('primero','segundo','postre') not null,
    primary key(nombre)
);

create table ingredientes(
    nombre varchar(50) not null,
    primary key(nombre)
);

create table platos_ingredientes(
    plato varchar(50) not null,
    ingrediente varchar(50) not null,
    primary key(plato, ingrediente),
    foreign key(plato) references platos(nombre) on delete cascade,
    foreign key(ingrediente) references ingredientes(nombre) on delete cascade
);

insert into ingredientes(nombre) values
('Tomate'),
('Lechuga'),
('Queso'),
('Jamón'),
('Pollo'),
('Carne'),
('Pescado'),
('Arroz'),
('Patatas'),
('Huevo');

insert into platos(nombre, precio, tipo) values
('Ensalada mixta', 5.50, 'primero'),
('Sopa de pollo', 4.00, 'primero'),
('Paella', 8.00, 'segundo'),
('Filete de ternera', 10.00, 'segundo'),
('Tarta de queso', 3.50, 'postre'),
('Helado', 2.50, 'postre');

insert into platos_ingredientes(plato, ingrediente) values
('Ensalada mixta', 'Tomate'),
('Ensalada mixta', 'Lechuga'),
('Ensalada mixta', 'Queso'),
('Sopa de pollo', 'Pollo'),
('Paella', 'Arroz'),
('Paella', 'Pescado'),
('Filete de ternera', 'Carne'),
('Tarta de queso', 'Queso'),
('Helado', 'Huevo');