package com.parthakadam.space.auth_service.services.accessIDService;


import com.parthakadam.space.auth_service.DTOs.SecretTokenDTO;
import com.parthakadam.space.auth_service.mappers.SecretTokenMapper;
import com.parthakadam.space.auth_service.models.SecretToken;
import com.parthakadam.space.auth_service.repositorys.SecretTokenRepository;
import com.parthakadam.space.auth_service.utils.Sha256Util;
import com.parthakadam.space.auth_service.utils.TokenGeneratorUtil;
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
    public SecretTokenDTO createAccessID(UUID bucketId) {
        String accessId = TokenGeneratorUtil.generateSecureToken(20);
        UUID accessIdUUID = UUID.fromString(accessId);

        String secretTokenStr = TokenGeneratorUtil.generateSecureToken(50);
        String secretTokenHash = Sha256Util.hash(secretTokenStr);
        SecretToken secretToken = secretTokenRepository.createAccessID(accessIdUUID,secretTokenHash,bucketId);

        SecretTokenDTO secretTokenDTO = SecretTokenMapper.toDTO(secretToken);

        return secretTokenDTO;
    }

    @Override
    public Boolean validateAccessID(UUID accessID , String accessToken) {
        List<SecretToken> secretTokens = secretTokenRepository.getSecretTokenByAccessKeyId(accessID);

        if(secretTokens.isEmpty()){
            return false;
        }

        SecretToken secretToken =  secretTokens.get(0);
        String accessTokenHash = Sha256Util.hash(accessToken);

        if(secretToken.getAccessKeyId().equals(accessTokenHash)){
            return true;
        }else {
            return false;
        }
    }
}
