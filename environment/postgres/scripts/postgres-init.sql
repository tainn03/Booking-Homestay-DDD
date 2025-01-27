-- CREATE TABLE IF NOT EXISTS test (id SERIAL PRIMARY KEY, name VARCHAR(50));

BEGIN;

-- structure setup

create table role (
                      role varchar(255) not null,
                      primary key (role)
);

-- data setup

INSERT INTO role (role)
VALUES ('ADMIN');
INSERT INTO role (role)
VALUES ('USER');
INSERT INTO role (role)
VALUES ('LANDLORD');

COMMIT;
