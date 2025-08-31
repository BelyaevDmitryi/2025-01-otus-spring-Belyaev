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

create table users (
    id bigserial,
    username varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);

create table authorities (
    id bigserial,
    authority varchar(255) not null,
    primary key (id)
);

create table user_authority_link (
    user_id bigint references users(id) on delete cascade,
    authority_id bigint references authorities(id) on delete cascade
);

CREATE TABLE acl_sid (
    id bigserial,
    principal smallint NOT NULL,
    sid varchar(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE acl_class (
    id bigserial,
    class varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE  acl_entry (
    id bigserial,
    acl_object_identity bigint NOT NULL,
    ace_order int NOT NULL,
    sid bigint NOT NULL,
    mask int NOT NULL,
    granting smallint NOT NULL,
    audit_success smallint NOT NULL,
    audit_failure smallint NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE acl_object_identity (
    id bigserial,
    object_id_class bigint NOT NULL,
    object_id_identity bigint NOT NULL,
    parent_object bigint DEFAULT NULL,
    owner_sid bigint DEFAULT NULL,
    entries_inheriting smallint NOT NULL,
    PRIMARY KEY (id)
);

alter table acl_sid add CONSTRAINT acl_sid_sid_principal_unk UNIQUE (sid,principal);
alter table acl_class add CONSTRAINT acl_class_class_unk UNIQUE (class);
alter table acl_entry add CONSTRAINT acl_entry_obj_order_unk UNIQUE (acl_object_identity,ace_order);
alter table acl_object_identity add CONSTRAINT acl_object_identity_class_iden_unk UNIQUE (object_id_class,object_id_identity);

ALTER TABLE acl_entry ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);
ALTER TABLE acl_entry ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);
ALTER TABLE acl_object_identity ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);
ALTER TABLE acl_object_identity ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);
ALTER TABLE acl_object_identity ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);

