create schema if not exists notification_schema;

create type notification_schema.notification_delivery_type as enum('Email' , 'Push');
create cast (varchar as notification_schema.notification_delivery_type) with inout as implicit;

create type notification_schema.template_status as enum('Created' , 'Archived');
create cast (varchar as notification_schema.template_status) with inout as implicit;

create table notification_schema.notification_type(
    id bigserial primary key,
    name notification_schema.notification_delivery_type not null
);

create table notification_schema.notification(
     id bigserial primary key,
     notification_type_id bigint references notification_schema.notification_type(id),
     name text not null,
     is_required boolean not null
);

create table notification_schema.user_notification_subscription(
    user_id bigserial not null,
    notification_id bigserial references notification_schema.notification(id),
    email text not null,
    active boolean not null,

    primary key (user_id, notification_id)
);

create table notification_schema.user_notification(
    id bigserial primary key,
    user_id bigint not null,
    notification_name text not null,
    message text not null,
    sent_at timestamp not null
);

create table notification_schema.template(
    id bigserial primary key,
    name text not null,
    content text not null,
    status notification_schema.template_status not null
);