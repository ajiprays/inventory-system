-----------PRODUCT SERVICE------------------------------------------------------------------------------
CREATE TABLE products (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    stock BIGINT NOT NULL,
    min_stock_threshold BIGINT NOT NULL,
    "version" BIGINT NOT NULL,
    CONSTRAINT products_pk PRIMARY KEY (id)
);

-------------STOCK CHANGE SERVICE----------------------------------------------------------------------------

CREATE TABLE stock_changes (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    product_id VARCHAR(255) NOT NULL,
    quantity BIGINT NOT NULL,
    change_type VARCHAR(50) NOT NULL,--SALE, RESTOCK
    new_stock BIGINT NOT NULL,
    "timestamp" timestamptz NOT NULL,
    CONSTRAINT stock_changes_pk PRIMARY KEY(id)
);

--------------ANALYTICS SERVICE---------------------------------------------------------------------------

CREATE TABLE products (
    id BIGINT NOT NULL ,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    CONSTRAINT products_pk PRIMARY KEY(id)
);

CREATE TABLE stock_changes (
    id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    change_type VARCHAR(50) NOT NULL,--SALE, RESTOCK
    new_stock BIGINT NOT NULL,
    "timestamp" timestamptz NOT NULL,
    CONSTRAINT stock_changes_pk PRIMARY KEY(id)
);

CREATE TABLE sales_records (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    product_id BIGINT NOT NULL,
    quantity_sold BIGINT NOT NULL,
    "timestamp" timestamptz NOT NULL,
    CONSTRAINT sales_records_pk PRIMARY KEY(id)
);

