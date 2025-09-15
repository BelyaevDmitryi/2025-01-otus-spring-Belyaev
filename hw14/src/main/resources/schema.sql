create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

create table book_comments (
    id bigserial,
    text varchar,
    book_id bigint references books(id) on delete cascade,
    primary key (id)
);

create table book_migrate (
    id bigint,
    mongo_id varchar,
    primary key (mongo_id)
);

create table author_migrate (
    id bigint,
    mongo_id varchar,
    primary key (mongo_id)
);
