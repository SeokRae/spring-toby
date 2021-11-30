CREATE TABLE IF NOT EXISTS USERS
(
    id       varchar(10),
    name     varchar(20) not null,
    password varchar(20) not null,
    primary key (id)
);

alter table USERS
    add column level tinyint not null default 0;
alter table USERS
    add column login int not null default 0;
alter table USERS
    add column recommend int not null default 0;