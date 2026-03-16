CREATE TABLE secret_token (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    access_key_id UUID NOT NULL UNIQUE,
    secret_access_key_hash VARCHAR(128) NOT NULL,

    bucket_id UUID NOT NULL, -- forein key of buckets table from object_store

    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires_at TIMESTAMPTZ,

    is_active BOOLEAN DEFAULT true
);