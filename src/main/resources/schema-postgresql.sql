CREATE TABLE IF NOT EXISTS tb_user (
    uid serial PRIMARY KEY,
    uname varchar(16) UNIQUE,
    pwd varchar(64)
);
CREATE EXTENSION IF NOT EXISTS pgcrypto;
