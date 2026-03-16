package com.parthakadam.space.auth_service.services.accessIDService;


import com.parthakadam.space.auth_service.DTOs.SecretTokenDTO;
import com.parthakadam.space.auth_service.mappers.SecretTokenMapper;
import com.parthakadam.space.auth_service.models.SecretToken;
import com.parthakadam.space.auth_service.repositorys.SecretTokenRepository;
import com.parthakadam.space.auth_service.utils.Sha256Util;
import com.parthakadam.space.auth_service.utils.TokenGeneratorUtil;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccessIDAuthSerivceImp  implements   AccessIDAuthSerivce {

    SecretTokenRepository secretTokenRepository;

    AccessIDAuthSerivceImp(SecretTokenRepository secretTokenRepository) {
        this.secretTokenRepository = secretTokenRepository;

    }

    @Override
    @Transactional
    public SecretTokenDTO createAccessID(UUID bucketId) {

        String secretTokenStr = TokenGeneratorUtil.generateSecureToken(50);
        String secretTokenHash = Sha256Util.hash(secretTokenStr);
        // SecretToken secretToken = secretTokenRepository.createAccessID(id,accessIdUUID,secretTokenHash,bucketId);

        // SecretTokenDTO secretTokenDTO = SecretTokenMapper.toDTO(secretToken);

        SecretToken token = SecretToken.builder()
        .id(UUID.randomUUID())
        .accessKeyId(UUID.randomUUID())
        .secretAccessKeyHash(secretTokenHash)
        .bucketId(bucketId)
        .build();


        SecretTokenDTO  secretTokenDTO =  SecretTokenMapper.toDTO(secretTokenRepository.save(token));
        secretTokenDTO.setSecretAccessKeyHash(secretTokenStr);
        return secretTokenDTO;
    }

    @Override
    public Boolean validateAccessID(UUID accessID, String accessToken) {

        List<SecretToken> secretTokens = secretTokenRepository.findByAccessKeyId(accessID);

        if (secretTokens.isEmpty()) {
            System.out.println("worng accessID or accessToken");
            return false;
        }
        SecretToken secretToken = secretTokens.getFirst();


        
        
        String accessTokenHash = Sha256Util.hash(accessToken);
        System.out.println(accessTokenHash);
        System.out.println(secretToken.getSecretAccessKeyHash());


        return secretToken.getSecretAccessKeyHash().equalsIgnoreCase(accessTokenHash);
    }
}
