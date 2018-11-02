create database WWBank;

create table Client(
    id INT primary key not null auto_increment,
    name VARCHAR(100) not null,
    address VARCHAR(100),
    age INT not null);

create table Account(
    id INT primary key not null auto_increment,
    id_client INT not null,
    money INT not null);

create table Transaction(
    id INT primary key not null auto_increment,
    id_sender INT not null,
    id_receiver INT not null,
    money INT not null,
    date VARCHAR(100) not null);

insert into Client(
    NULL,
    "BANK",
    "Wild West",
    25);

insert into Account(
    NULL,
    1,
    99999);
