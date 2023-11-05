drop table if exists app_user__role;
drop table if exists role;
drop table if exists app_user;

create table app_user
(
    id       serial primary key,
    username varchar(100) not null,
    password varchar(255) not null,
    name     varchar(100) not null,
    email    varchar(100) not null
);

create table role
(
    id   serial primary key,
    name varchar(100)
);

create table app_user__role
(
    app_user_id int not null,
    role_id     int not null,
    foreign key (app_user_id) references app_user (id),
    foreign key (role_id) references role (id),
    primary key (app_user_id, role_id)
);

insert into app_user(username, password, name, email)
values ('maximus', '$2a$10$PdYtIQYstvMYdtDuytzTJ.XyBRINgpCWPIcyi2R/txXuRPkDwcFSG', 'Maximus', 'mxms@gmail.com'),
       ('commodus', '$2a$10$YaMK5THDlDuvDYCrsaUBBuuFbqO8Q.2rVNBPT9i06U7YlEP9ZL1V6', 'Commodus', 'cmdus@gmail.com');

insert into role(name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');

insert into app_user__role(app_user_id, role_id)
values (1, 1),
       (2, 2);