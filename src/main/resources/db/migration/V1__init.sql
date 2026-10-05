CREATE TABLE users (
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    nickname VARCHAR(50)  NOT NULL,
    role     VARCHAR(20)  NOT NULL
);

CREATE TABLE posts (
    id         BIGSERIAL PRIMARY KEY,
    author_id  BIGINT       NOT NULL REFERENCES users (id),
    title      VARCHAR(200) NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP    NOT NULL
);
