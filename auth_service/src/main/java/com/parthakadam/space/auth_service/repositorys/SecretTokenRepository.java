package com.parthakadam.space.auth_service.repositorys;

import com.parthakadam.space.auth_service.models.SecretToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SecretTokenRepository  extends JpaRepository<SecretToken, UUID> {

    
    @Transactional
    @Query(
            value = "INSERT INTO secret_token (id,access_key_id, secret_access_key_hash, bucket_id) VALUES (:id, :access_key_id, :secret_access_key_hash, :bucket_id) RETURNING *",
            nativeQuery = true
    )
    SecretToken createAccessID(
            @Param("id") UUID id,
            @Param("access_key_id") UUID accessKeyId,
            @Param("secret_access_key_hash") String secretAccessKeyHash,
            @Param("bucket_id") UUID bucketId
    );

    List<SecretToken> findByAccessKeyId(UUID accessKeyId);
}
