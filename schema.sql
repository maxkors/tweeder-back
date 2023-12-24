drop table if exists subscription;
drop table if exists comment;
-- drop table if exists retweet;
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

create table tweet
(
    id          serial primary key,
    app_user_id int       not null,
    text        varchar(280),
    likes       int       not null,
    date_time   timestamp not null,
    foreign key (app_user_id) references app_user (id)
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
    likes       int       not null,
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

insert into tweet(app_user_id, text, likes, date_time)
values (1, 'Salut', 87, '2023-06-22 19:10:25-07'),
       (1, 'Whats new?', 23, '2023-06-20 13:10:25-07'),
       (2, 'Halo', 54, '2023-03-22 11:10:25-07'),
       (2, 'Just look at this:', 10, '2023-03-22 11:10:25-07'),
       (2, 'What are u doing rn?', 10, '2023-03-22 11:10:25-07'),
       (3, 'There is nothing impossible to him who will try', 128, '2023-08-10 16:10:25-07');

-- insert into retweet
-- values (1, 3, 1, 'Ciao', 5, '2023-06-22 19:15:25-07');

insert into comment(tweet_id, app_user_id, text, likes, date_time)
values (5, 1, 'Listening to music', 10, '2023-06-22 19:15:25-07'),
       (5, 2, 'Nice', 3, '2023-06-22 19:17:25-07'),
       (2, 3, 'Sup?', 5, '2023-06-22 19:17:25-07');

insert into subscription
values (1, 2),
       (2, 1),
       (3, 1);

-- select id, NULL as tweet_id, app_user_id, text, likes, date_time from tweet t
-- where t.app_user_id = 1;
--
-- select * from retweet rt
-- inner join tweet t on rt.tweet_id = t.id
-- where rt.app_user_id = 1;

select u.name, count(s.follower_id) as subscribers, count(s.subject_id) as subscriptions
from app_user u
         left join subscription s on u.id in (s.follower_id, s.subject_id)
where u.username = 'maximus'
group by u.id, u.name;

select u.id,
       u.username,
       u.name,
       count(case u.id when s.subject_id then 1 end)  as subscribersCount,
       count(case u.id when s.follower_id then 1 end) as subscriptionsCount
from app_user u
         left join subscription s on u.id in (s.subject_id, s.follower_id)
where u.username = 'maximus'
group by u.id
;