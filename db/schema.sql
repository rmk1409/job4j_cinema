DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS account;

CREATE TABLE IF NOT EXISTS account
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    email    VARCHAR NOT NULL UNIQUE,
    phone    VARCHAR NOT NULL UNIQUE
);

INSERT INTO account(username, email, phone)
VALUES ('name1', 'email1', '1');
INSERT INTO account(username, email, phone)
VALUES ('name2', 'email2', '2');
INSERT INTO account(username, email, phone)
VALUES ('name3', 'email3', '3');

CREATE TABLE IF NOT EXISTS ticket
(
    id         SERIAL PRIMARY KEY,
    session_id INT NOT NULL DEFAULT 1,
    row        INT NOT NULL,
    cell       INT NOT NULL,
    account_id INT NOT NULL REFERENCES account (id),
    UNIQUE (session_id, row, cell)
);

INSERT INTO ticket(row, cell, account_id)
VALUES (1, 1, 1);
INSERT INTO ticket(row, cell, account_id)
VALUES (1, 2, 2);
