CREATE TABLE buckets (
    id              UUID PRIMARY KEY,
    name            VARCHAR(63) NOT NULL UNIQUE,
    region          VARCHAR(32) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE objects (
    id              UUID PRIMARY KEY,
    bucket_id       UUID NOT NULL REFERENCES buckets(id) ON DELETE CASCADE,
    object_key      TEXT NOT NULL,
    data_path       TEXT NOT NULL,         -- where binary is stored
    size_bytes      BIGINT NOT NULL,
    content_type    VARCHAR(255),
    checksum_sha256 VARCHAR(128),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),

    UNIQUE (bucket_id, object_key)
);

CREATE TABLE object_metadata (
    object_id   UUID NOT NULL REFERENCES objects(id) ON DELETE CASCADE,
    meta_key    VARCHAR(128) NOT NULL,
    meta_value  TEXT,

    PRIMARY KEY (object_id, meta_key)
);

CREATE TABLE object_versions (
    id              UUID PRIMARY KEY,
    object_id       UUID NOT NULL REFERENCES objects(id) ON DELETE CASCADE,
    version_number  INTEGER NOT NULL,
    data_path       TEXT NOT NULL,
    size_bytes      BIGINT NOT NULL,
    checksum_sha256 VARCHAR(128),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),

    UNIQUE (object_id, version_number)
);

CREATE INDEX idx_objects_bucket ON objects(bucket_id);
CREATE INDEX idx_objects_key ON objects(object_key);
CREATE INDEX idx_metadata_key ON object_metadata(meta_key);



