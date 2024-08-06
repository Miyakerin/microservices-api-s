ALTER TABLE client
ADD is_deleted BOOLEAN not null DEFAULT false;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS mail(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id BIGINT not null REFERENCES client (id) ON DELETE CASCADE,
    exp TIMESTAMP not null
)
