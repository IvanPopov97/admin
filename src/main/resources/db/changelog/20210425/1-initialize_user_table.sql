--liquibase formatted sql

--changeset popov:20210425-1
create type user_role as enum ('USER', 'ADMIN');

--changeset popov:20210425-2
create type user_status as enum ('NOT_ACTIVE', 'ACTIVE', 'BLOCKED');

--changeset popov:20210425-3
create table if not exists users
(
	id bigserial
		constraint users_pk
			primary key,
	email text not null,
	password text not null,
	role user_role default 'USER',
	status user_status default 'NOT_ACTIVE'
);

create unique index if not exists users_email_index
	on users (email);

comment on table users is 'Таблица пользователей';

