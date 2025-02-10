-- CREATE TABLE IF NOT EXISTS test (id SERIAL PRIMARY KEY, name VARCHAR(50));

BEGIN;

-- structure setup

create table if not exists public.role (
      role varchar(255) not null,
      primary key (role)
);

create table if not exists public.permission
(
    permission varchar(255) not null
    primary key,
    api_path   varchar(255),
    method     varchar(255),
    module     varchar(255)
    );

-- data setup

INSERT INTO role (role)
VALUES ('ADMIN');
INSERT INTO role (role)
VALUES ('USER');
INSERT INTO role (role)
VALUES ('LANDLORD');

COMMIT;
