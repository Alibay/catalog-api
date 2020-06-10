BEGIN;

ALTER TABLE categories
    DROP CONSTRAINT categories_parent_id_fk,
    ADD CONSTRAINT categories_parent_id_fk
        FOREIGN KEY (parent_id)
        REFERENCES categories(id)
        ON DELETE cascade;

ALTER TABLE products
    DROP CONSTRAINT products_category_id_fk,
    ADD CONSTRAINT products_category_id_fk
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
        ON DELETE cascade;

COMMIT;