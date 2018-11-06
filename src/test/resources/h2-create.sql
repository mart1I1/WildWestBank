create table Client(
    id INT not null auto_increment primary key,
    name VARCHAR(100) not null,
    address VARCHAR(100),
    age INT not null);

create table Account(
    id INT not null auto_increment primary key ,
    id_client INT not null,
    money DOUBLE not null);

create table Transaction(
    id INT not null auto_increment primary key,
    id_sender INT not null,
    id_receiver INT not null,
    money INT not null,
    date VARCHAR(100) not null);

insert into Client values(
    NULL,
    'BANK',
    'Wild West',
    25);

insert into Account values(
    NULL,
    1,
    99999);