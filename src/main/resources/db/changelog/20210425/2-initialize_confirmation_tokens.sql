--liquibase formatted sql

--changeset popov:20210513-1
create table if not exists confirmation_tokens
(
	id bigserial
		constraint confirmation_tokens_pk
			primary key,
	code text not null,
	created_at timestamp default now(),
	expires_at timestamp not null,
	confirmed_at timestamp,
	user_id bigint not null
		constraint confirmation_tokens_fk
			references users
				on update cascade on delete cascade,
	action text not null
);

comment on table confirmation_tokens is 'Токены подтверждения действий пользователя';

comment on column confirmation_tokens.code is 'Секретный код';

comment on column confirmation_tokens.action is 'Действие, которое пользователь должен подтвердить';

create unique index if not exists confirmation_tokens_code_index
	on confirmation_tokens (code);
