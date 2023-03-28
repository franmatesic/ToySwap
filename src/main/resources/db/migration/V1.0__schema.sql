create table if not exists app_user
(
    id              integer primary key generated always as identity,
    email           varchar(50) not null,
    password        text        not null,
    first_name      varchar(50),
    last_name       varchar(50),
    role            varchar(50) not null,
    phone_number    varchar(16),
    profile_picture bytea
);

create table if not exists message
(
    id           integer primary key generated always as identity,
    from_user_id integer   not null,
    to_user_id   integer   not null,
    content      text      not null,
    created_at   timestamp not null,
    constraint message_from_user_fk foreign key (from_user_id) references app_user (id),
    constraint message_to_user_fk foreign key (to_user_id) references app_user (id)
);

create table if not exists address
(
    id          integer primary key generated always as identity,
    country     varchar(100) not null,
    town        varchar(100) not null,
    postal_code varchar(10)  not null,
    address     varchar(100) not null,
    created_at  timestamp    not null
);

create table if not exists credit_card
(
    id         integer primary key generated always as identity,
    number     varchar(100) not null,
    holder     varchar(100) not null,
    back_code  varchar(10)  not null,
    created_at timestamp    not null
);

create table if not exists payment_method
(
    id             integer primary key generated always as identity,
    user_id        integer     not null,
    address_id     integer,
    credit_card_id integer,
    type           varchar(50) not null,
    created_at     timestamp   not null,
    updated_at     timestamp   not null,
    constraint payment_method_user_fk foreign key (user_id) references app_user (id),
    constraint payment_method_address_fk foreign key (address_id) references address (id),
    constraint payment_method_credit_card_fk foreign key (credit_card_id) references credit_card (id)
);

create table if not exists app_tag
(
    id   integer primary key generated always as identity,
    name varchar(50) not null
);

create table if not exists post
(
    id          integer primary key generated always as identity,
    user_id     integer       not null,
    title       varchar(100)  not null,
    description varchar(300),
    price       decimal(5, 2) not null,
    condition   varchar(50)   not null,
    created_at  timestamp     not null,
    updated_at  timestamp     not null,
    constraint post_user_fk foreign key (user_id) references app_user (id)
);

create table if not exists post_tag
(
    id      integer primary key generated always as identity,
    post_id integer not null,
    tag_id  integer not null,
    constraint post_tag_post_fk foreign key (post_id) references post (id),
    constraint post_tag_tag_fk foreign key (tag_id) references app_tag (id)
);

create table if not exists post_image
(
    id      integer primary key generated always as identity,
    post_id integer not null,
    image   bytea   not null,
    constraint post_image_post_fk foreign key (post_id) references post (id)
);

create table if not exists transaction_request
(
    id                integer primary key generated always as identity,
    user_id           integer   not null,
    post_id           integer   not null,
    payment_method_id integer   not null,
    created_at        timestamp not null,
    accepted_at       timestamp,
    denied_at         timestamp,
    constraint transaction_request_user_fk foreign key (user_id) references app_user (id),
    constraint transaction_request_post_fk foreign key (post_id) references post (id),
    constraint transaction_request_payment_method_fk foreign key (payment_method_id) references payment_method (id)
);