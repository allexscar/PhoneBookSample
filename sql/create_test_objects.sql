--
-- PostgreSQL test schema objects creation
--

--create role testuser
CREATE ROLE testuser WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  CREATEDB
  NOCREATEROLE
  NOREPLICATION
  NOBYPASSRLS
  ENCRYPTED PASSWORD 'SCRAM-SHA-256$4096:dZvrnlOpZu00jyDp5UCaTA==$GnowUMT9aZUnPZa5dDjBJV24oGIbdbE26sFE8GnHL48=:+pW2RpnEC44doWz1YcI5qx9WiYM9L37ZWcLSZntFuCg=';
GRANT postgres TO testuser WITH ADMIN OPTION;

--create test schema
CREATE SCHEMA IF NOT EXISTS test;
ALTER SCHEMA test OWNER TO testuser;

--create test table phones
CREATE TABLE test.phones (
                             id bigint NOT NULL,
                             phone character varying(20),
                             user_id bigint NOT NULL
);
ALTER TABLE test.phones OWNER TO testuser;
ALTER TABLE test.phones ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME test.phones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    );

--create test table users
CREATE TABLE test.users (
    id bigint NOT NULL,
    name character varying(50)
);
ALTER TABLE test.users OWNER TO testuser;
ALTER TABLE test.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME test.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

--create constraints
ALTER TABLE ONLY test.users
    ADD CONSTRAINT pk_id_user PRIMARY KEY (id);
ALTER TABLE ONLY test.phones
    ADD CONSTRAINT pk_id_phone PRIMARY KEY (id);
ALTER TABLE IF EXISTS test.phones
    ADD CONSTRAINT fk_phone2user FOREIGN KEY (user_id)
        REFERENCES test.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE;

