BEGIN;

CREATE SEQUENCE products_id_seq INCREMENT BY 50;
CREATE SEQUENCE categories_id_seq INCREMENT BY 1;

CREATE TABLE categories
(
    id BIGINT DEFAULT NEXTVAL('categories_id_seq') CONSTRAINT categories_pk PRIMARY KEY,
    name varchar(255) NOT NULL,
    parent_id bigint DEFAULT NULL CONSTRAINT categories_parent_id_fk REFERENCES categories
);

CREATE TABLE products
(
    id BIGINT DEFAULT  NEXTVAL('products_id_seq') CONSTRAINT products_pk PRIMARY KEY,
    name varchar(255) NOT NULL,
    category_id bigint NOT NULL CONSTRAINT products_category_id_fk REFERENCES categories,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP
);

COMMIT;