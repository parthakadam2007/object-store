CREATE TABLE replicas(
    id SERIAL  PRIMARY KEY,
    object_id UUID NOT NULL  ,
    data_path       TEXT NOT NULL,         -- where binary is stored
    size_bytes      BIGINT NOT NULL,
    content_type    VARCHAR(255),
    checksum_sha256 VARCHAR(128),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_object_id
    FOREIGN KEY (object_id)
    REFERENCES objects(id)
    ON DELETE CASCADE

)