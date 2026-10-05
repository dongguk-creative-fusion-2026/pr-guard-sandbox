CREATE TABLE comments (
    id         BIGSERIAL PRIMARY KEY,
    post_id    BIGINT        NOT NULL REFERENCES posts (id) ON DELETE CASCADE,
    author_id  BIGINT        NOT NULL REFERENCES users (id),
    content    VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP     NOT NULL
);

CREATE INDEX comments_post_id_idx ON comments (post_id);
