create sequence user_id_seq start with 1 increment by 50;
create sequence post_id_seq start with 1 increment by 50;
create sequence cat_id_seq start with 1 increment by 50;

create table users
(
    id         bigint    not null default nextval('user_id_seq'),
    email      text      not null,
    password   text      not null,
    name       text      not null,
    role       text      not null,
    created_at timestamp not null default now(),
    updated_at timestamp,
    primary key (id),
    constraint user_email_unique unique (email)
);

create table categories
(
    id         bigint    not null default nextval('cat_id_seq'),
    name       text      not null,
    created_at timestamp not null default now(),
    updated_at timestamp,
    primary key (id),
    constraint category_name_unique unique (name)
);

create table posts
(
    id         bigint    not null default nextval('post_id_seq'),
    url        text      not null,
    title      text,
    cat_id     bigint    not null references categories (id),
    created_by bigint    not null references users (id),
    created_at timestamp not null default now(),
    updated_at timestamp,
    primary key (id)
);
