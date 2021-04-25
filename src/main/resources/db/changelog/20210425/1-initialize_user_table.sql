--liquibase formatted sql

--changeset popov:20210425-1
create table if not exists users
(
	id bigserial
		constraint users_pk
			primary key,
	email text not null,
	password text not null,
	role text default 'USER',
	status text default 'NOT_ACTIVE'
);

create unique index if not exists users_email_index
	on users (email);

comment on table users is 'Таблица пользователей';

insert into users (email, password, role, status) values ('golden.royal@mail.ru', '{noop}31165119221', 'ADMIN', 'ACTIVE')
