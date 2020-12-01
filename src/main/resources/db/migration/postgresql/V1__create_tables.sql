create sequence user_id_seq start with 1 increment by 100;
create sequence link_id_seq start with 1 increment by 100;
create sequence tag_id_seq start with 1 increment by 100;

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

create table links (
    id bigint DEFAULT nextval('link_id_seq') not null,
    url varchar(1024) not null,
    title varchar(1024),
    created_by bigint not null REFERENCES users(id),
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);

create table tags (
    id bigint DEFAULT nextval('tag_id_seq') not null,
    name varchar(255) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id),
    CONSTRAINT tag_name_unique UNIQUE(name)
);

create table link_tag (
    link_id bigint not null REFERENCES links(id),
    tag_id bigint not null REFERENCES tags(id)
);
