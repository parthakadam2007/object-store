package com.parthakadam.space.object_store.models;

import java.util.UUID;

public class BodyAccessCreateAuth {
    public UUID bucketId;

    public BodyAccessCreateAuth(UUID bucketId) {
        this.bucketId = bucketId;
    }

}
