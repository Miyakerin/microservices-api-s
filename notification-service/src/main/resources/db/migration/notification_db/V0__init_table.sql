CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS links(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id BIGINT not null unique
)