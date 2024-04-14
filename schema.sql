drop table if exists message;
drop table if exists app_user__chat;
drop table if exists chat;
drop table if exists bookmark;
drop table if exists user_like;
drop table if exists subscription;
drop table if exists comment;
-- drop table if exists retweet;
drop table if exists tweet_child;
drop table if exists media;
drop table if exists tweet;
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

-- create table tweet
-- (
--     id            serial primary key,
--     app_user_id   int       not null,
--     text          varchar(280),
--     likes_count    int       not null,
--     comments_count int       not null,
--     date_time     timestamp not null,
--     foreign key (app_user_id) references app_user (id)
-- );

create table tweet
(
    id             serial primary key,
    app_user_id    int       not null,
    text           varchar(280),
    likes_count    int       not null,
    comments_count int       not null,
    date_time      timestamp not null,
    parent_id      int,
    foreign key (app_user_id) references app_user (id)
);

create table media
(
    id       serial primary key,
    type     varchar(20)  not null,
    urn      varchar(100) not null,
    tweet_id int          not null,
    foreign key (tweet_id) references tweet (id)
);

create table tweet_child
(
    tweet_id int not null,
    child_id int not null,
    foreign key (tweet_id) references tweet (id),
    foreign key (child_id) references tweet (id),
    primary key (tweet_id, child_id)
);

-- create table retweet
-- (
--     id          serial primary key,
--     tweet_id    int       not null,
--     app_user_id int       not null,
--     text        varchar(280),
--     likes       int       not null,
--     date_time   timestamp not null,
--     foreign key (tweet_id) references tweet (id),
--     foreign key (app_user_id) references app_user (id)
-- );

create table comment
(
    id          serial primary key,
    tweet_id    int       not null,
--     retweet_id  int       not null,
    app_user_id int       not null,
    text        varchar(280),
    likes_count int       not null,
    date_time   timestamp not null,
    foreign key (tweet_id) references tweet (id),
--     foreign key (retweet_id) references retweet (id),
    foreign key (app_user_id) references app_user (id)
);

create table subscription
(
    follower_id int not null,
    subject_id  int not null,
    foreign key (follower_id) references app_user (id),
    foreign key (subject_id) references app_user (id),
    primary key (follower_id, subject_id)
);

create table user_like
(
    app_user_id int not null,
    tweet_id    int not null,
    foreign key (app_user_id) references app_user (id),
    foreign key (tweet_id) references tweet (id),
    primary key (app_user_id, tweet_id)
);

create table bookmark
(
    app_user_id int not null,
    tweet_id    int not null,
    foreign key (app_user_id) references app_user (id),
    foreign key (tweet_id) references tweet (id),
    primary key (app_user_id, tweet_id)
);

create table chat
(
    id serial primary key
);

create table app_user__chat
(
    app_user_id int not null,
    chat_id int not null,
    foreign key (app_user_id) references app_user (id),
    foreign key (chat_id) references chat (id),
    primary key (app_user_id, chat_id)
);

create table message
(
    id serial primary key,
    sender_id int not null,
    chat_id int not null,
    text varchar(200),
    foreign key (sender_id) references app_user (id),
    foreign key (chat_id) references chat (id)
);


insert into app_user(username, password, name, email)
values ('maximus', '$2a$10$PdYtIQYstvMYdtDuytzTJ.XyBRINgpCWPIcyi2R/txXuRPkDwcFSG', 'Maximus', 'mxms@gmail.com'),
       ('commodus', '$2a$10$YaMK5THDlDuvDYCrsaUBBuuFbqO8Q.2rVNBPT9i06U7YlEP9ZL1V6', 'Commodus', 'cmdus@gmail.com'),
       ('alexander', '$2a$10$qGFTYYDxRNs0iySmRy.Nxu9J7EyUu2BWtJcAnjJ7wh2yPWYi35FiW', 'Alexander', 'alex@gmail.com');

insert into role(name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');

insert into app_user__role(app_user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 2);

-- insert into tweet(app_user_id, text, likes_count, comments_count, date_time)
-- values (1, 'Salut', 87, 1, '2023-06-22 19:10:25-07'),
--        (1, 'Whats new?', 23, 1, '2023-06-20 13:10:25-07'),
--        (2, 'Halo', 54, 0, '2023-03-22 11:10:25-07'),
--        (2, 'Just look at this:', 10, 0, '2023-03-22 11:10:25-07'),
--        (2, 'What are u doing rn?', 10, 2, '2023-03-22 11:10:25-07'),
--        (3, 'There is nothing impossible to him who will try', 128, 0, '2023-08-10 16:10:25-07');

insert into tweet(app_user_id, text, likes_count, comments_count, date_time, parent_id)
values (1, 'Salut', 2, 2, '2023-06-22 19:10:25-07', null),
       (1, 'Whats new?', 1, 0, '2023-06-20 13:10:25-07', 1),
       (2, 'Halo', 1, 0, '2023-03-22 11:10:25-07', 1),
       (2, 'Just look at this:', 0, 0, '2023-03-22 11:10:25-07', null),
       (2, 'What are u doing rn?', 0, 0, '2023-03-22 11:10:25-07', null),
       (3, 'There is nothing impossible to him who will try', 0, 0, '2023-08-10 16:10:25-07', null);

-- insert into retweet
-- values (1, 3, 1, 'Ciao', 5, '2023-06-22 19:15:25-07');

insert into media (type, urn, tweet_id)
values ('image', 'room1-1.webp', 1),
       ('image', 'room2-1.webp', 2),
       ('image', 'room2-2.webp', 2),
       ('image', 'room4-1.webp', 4),
       ('image', 'room4-2.webp', 4),
       ('image', 'room4-3.webp', 4),
       ('image', 'room4-4.webp', 4),
       ('image', 'room5-1.webp', 5),
       ('image', 'room5-2.webp', 5),
       ('image', 'room5-3.webp', 5);

insert into comment(tweet_id, app_user_id, text, likes_count, date_time)
values (5, 1, 'Listening to music', 10, '2023-06-22 19:15:25-07'),
       (5, 2, 'Nice', 3, '2023-06-22 19:17:25-07'),
       (2, 3, 'Sup?', 5, '2023-06-22 19:17:25-07'),
       (1, 3, 'Salam', 1, '2023-06-22 19:17:25-07');

insert into subscription
values (1, 2),
       (1, 3),
       (2, 1),
       (3, 1);

insert into user_like
values (1, 1),
       (1, 3),
       (2, 1),
       (2, 2);

insert into bookmark
values (1, 1),
       (1, 4);

insert into chat
values (nextval('chat_id_seq'));

insert into app_user__chat
values (1, 1),
       (2, 1);

insert into message(sender_id, chat_id, text)
values (1, 1, 'hi'),
       (2, 1, 'halo');


-- select id, NULL as tweet_id, app_user_id, text, likes, date_time from tweet t
-- where t.app_user_id = 1;
--
-- select * from retweet rt
-- inner join tweet t on rt.tweet_id = t.id
-- where rt.app_user_id = 1;

-- select u.name, count(s.follower_id) as subscribers, count(s.subject_id) as subscriptions
-- from app_user u
--          left join subscription s on u.id in (s.follower_id, s.subject_id)
-- where u.username = 'maximus'
-- group by u.id, u.name;
--
-- select u.id,
--        u.username,
--        u.name,
--        count(case u.id when s.subject_id then 1 end)  as subscribersCount,
--        count(case u.id when s.follower_id then 1 end) as subscriptionsCount
-- from app_user u
--          left join subscription s on u.id in (s.subject_id, s.follower_id)
-- where u.username = 'maximus'
-- group by u.id;
--
-- select t.id, t.app_user_id, t.text, t.likes, t.date_time, count(c) as commentsCount
-- from app_user u
--          left join subscription s on u.id = s.follower_id
--          left join app_user su on s.subject_id = su.id
--          left join tweet t on su.id = t.app_user_id
--          left join comment c on t.id = c.tweet_id
-- where u.username = 'maximus'
-- group by t.id, t.date_time
-- order by t.date_time desc;

