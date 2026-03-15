package com.parthakadam.space.auth_service.repositorys;

import com.parthakadam.space.auth_service.models.SecretToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SecretTokenRepository  extends JpaRepository<SecretToken, UUID> {

    @Query(
            value = "INSERT INTO secret_token (access_key_id ,secret_access_key_hash ,bucket_id) VALUES (:access_key_id , :secret_access_key_hash , :bucket_id)",
            nativeQuery = true
    )
    SecretToken createAccessID(
            @Param("access_key_id") UUID accessKeyId,
            @Param("secret_access_key_hash") String secretAccessKeyHash,
            @Param("bucket_id") UUID bucketId
    );

    @Query(
            value = "SELECT * FROM secret_token WHERE access_key_id = :access_key_id",
            nativeQuery = true
    )
    List<SecretToken> getSecretTokenByAccessKeyId(
            @Param("access_key_id")   UUID accessKeyId
    );
}
