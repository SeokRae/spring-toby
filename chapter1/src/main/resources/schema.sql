CREATE TABLE IF NOT EXISTS USERS
(
    id       varchar(10),
    name     varchar(20) not null,
    password varchar(20) not null,
    primary key (id)
);