create sequence user_id_seq start with 1 increment by 50;
create sequence link_id_seq start with 1 increment by 50;
create sequence cat_id_seq start with 1 increment by 50;

create table users (
    id bigint DEFAULT nextval('user_id_seq') not null,
    email varchar(255) not null,
    password varchar(255) not null,
    name varchar(255) not null,
    image_url varchar(255),
    role varchar(20) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id),
    CONSTRAINT user_email_unique UNIQUE(email)
);

create table categories (
    id bigint DEFAULT nextval('cat_id_seq') not null,
    name varchar(255) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id),
    CONSTRAINT category_name_unique UNIQUE(name)
);

create table links (
    id bigint DEFAULT nextval('link_id_seq') not null,
    url varchar(1024) not null,
    title varchar(1024),
    created_by bigint not null REFERENCES users(id),
    cat_id bigint not null REFERENCES categories(id),
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);
