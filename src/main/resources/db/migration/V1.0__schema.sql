create table if not exists app_user
(
    id              integer primary key generated always as identity,
    email           varchar(50) not null,
    password        text        not null,
    first_name      varchar(50),
    last_name       varchar(50),
    phone_number    varchar(16),
    profile_picture bytea
);