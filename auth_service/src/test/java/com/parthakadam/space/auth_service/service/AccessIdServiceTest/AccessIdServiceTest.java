package com.parthakadam.space.auth_service.service.AccessIdServiceTest;

import com.parthakadam.space.auth_service.models.SecretToken;
import com.parthakadam.space.auth_service.repositorys.SecretTokenRepository;
import com.parthakadam.space.auth_service.services.accessIDService.AccessIDAuthSerivceImp;
import com.parthakadam.space.auth_service.utils.Sha256Util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AccessIdServiceTest {

    @Mock
    private SecretTokenRepository secretTokenRepository;

    @InjectMocks
    private AccessIDAuthSerivceImp accessIdService;

    

    @Test
    void validateAccessID_shouldReturnTrue_whenTokenMatches() {
        
        UUID accessID = UUID.randomUUID();
        UUID bucketId = UUID.randomUUID();
        String token = "secret";

        String hashed = Sha256Util.hash(token);

        SecretToken secretToken = new SecretToken();
        secretToken.setSecretAccessKeyHash(hashed);
        secretToken.setBucketId(bucketId);

        when(secretTokenRepository.findByAccessKeyId(accessID))
                .thenReturn(List.of(secretToken));

        boolean result = accessIdService.validateAccessID(accessID, bucketId, token);

        assertTrue(result);
    }

    @Test
    void validateAccessID_shouldReturnFalse_whenTokenMismatch() {

        UUID accessID = UUID.randomUUID();
        UUID bucketId = UUID.randomUUID();

        SecretToken secretToken = new SecretToken();
        secretToken.setSecretAccessKeyHash("wronghash");
        secretToken.setBucketId(bucketId);

        when(secretTokenRepository.findByAccessKeyId(accessID))
                .thenReturn(List.of(secretToken));

        boolean result = accessIdService.validateAccessID(accessID, bucketId, "secret");

        assertFalse(result);
    }

    @Test
    void validateAccessID_shouldReturnFalse_whenBucketMismatch() {

        UUID accessID = UUID.randomUUID();
        UUID bucketId = UUID.randomUUID();
        UUID wrongBucket = UUID.randomUUID();

        String token = "secret";
        String hashed = Sha256Util.hash(token);

        SecretToken secretToken = new SecretToken();
        secretToken.setSecretAccessKeyHash(hashed);
        secretToken.setBucketId(wrongBucket);

        when(secretTokenRepository.findByAccessKeyId(accessID))
                .thenReturn(List.of(secretToken));

        boolean result = accessIdService.validateAccessID(accessID, bucketId, token);

        assertFalse(result);
    }

    @Test
    void validateAccessID_shouldReturnFalse_whenNoTokenFound() {

        UUID accessID = UUID.randomUUID();

        when(secretTokenRepository.findByAccessKeyId(accessID))
                .thenReturn(Collections.emptyList());

        boolean result = accessIdService.validateAccessID(accessID, UUID.randomUUID(), "token");

        assertFalse(result);
    }

    @Test
    void validateAccessID_shouldThrowException_whenRepositoryFails() {

        UUID accessID = UUID.randomUUID();

        when(secretTokenRepository.findByAccessKeyId(accessID))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () ->
                accessIdService.validateAccessID(accessID, UUID.randomUUID(), "token"));
    }


}