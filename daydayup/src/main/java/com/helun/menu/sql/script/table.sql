drop database if exists free_menu;
create database free_menu ;

create table if not exists menu(
	menuId char(36) not null,
	name varchar(50) not null,
	menuType tinyint not null,
	description varchar(240) not null,
	PRIMARY KEY (menuId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
