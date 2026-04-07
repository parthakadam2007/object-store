/*
 * Copyright (c) 2026 Partha Kadam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.parthakadam.space.auth_service.services.accessIDService;


import com.parthakadam.space.auth_service.AuthServiceApplication;
import com.parthakadam.space.auth_service.DTOs.SecretTokenDTO;
import com.parthakadam.space.auth_service.exceptions.accessTokenExceptions.AccessTokenInfoSavingException;
import com.parthakadam.space.auth_service.exceptions.accessTokenExceptions.AccessTokenValidatingException;
import com.parthakadam.space.auth_service.mappers.SecretTokenMapper;
import com.parthakadam.space.auth_service.models.SecretToken;
import com.parthakadam.space.auth_service.repositorys.SecretTokenRepository;
import com.parthakadam.space.auth_service.utils.Sha256Util;
import com.parthakadam.space.auth_service.utils.TokenGeneratorUtil;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccessIDAuthSerivceImp  implements  AccessIDAuthSerivce {

    private  static  final Logger logger = LoggerFactory.getLogger(AuthServiceApplication.class);
    private  SecretTokenRepository secretTokenRepository;

    AccessIDAuthSerivceImp(SecretTokenRepository secretTokenRepository) {
        this.secretTokenRepository = secretTokenRepository;

    }

    @Override
    @Transactional
    public SecretTokenDTO createAccessID(UUID bucketId) {
        logger.debug("createAccessID");

        String secretTokenStr = TokenGeneratorUtil.generateSecureToken(50);
        String secretTokenHash = Sha256Util.hash(secretTokenStr);

        SecretToken token = SecretToken.builder()
        .id(UUID.randomUUID())
        .accessKeyId(UUID.randomUUID())
        .secretAccessKeyHash(secretTokenHash)
        .bucketId(bucketId)
        .build();

        SecretTokenDTO  secretTokenDTO;

        try{
            secretTokenDTO =  SecretTokenMapper.toDTO(secretTokenRepository.save(token));
        } catch (Exception e) {
            logger.error("buckerId: " + bucketId);
            throw new AccessTokenInfoSavingException("Exception when creating Access token",e);
        }

        secretTokenDTO.setSecretAccessKeyHash(secretTokenStr);
        return secretTokenDTO;
    }

    @Override
    public Boolean validateAccessID(UUID accessID, UUID bucketId, String accessToken) throws AccessTokenValidatingException{

        List<SecretToken> secretTokens;

        try {
            secretTokens = secretTokenRepository.findByAccessKeyId(accessID);
        } catch (Exception e) {
            logger.error("Error fetching secret tokens | accessID={} bucketId={}", accessID, bucketId, e);
            throw new AccessTokenValidatingException("Exception when validating access token", e);
        }

        if (secretTokens.isEmpty()) {
            logger.warn("No secret tokens found | accessID={}", accessID);
            return false;
        }

        SecretToken secretToken = secretTokens.getFirst();

        String accessTokenHash = Sha256Util.hash(accessToken);

        logger.debug("Validating token | accessID={} bucketId={}", accessID, bucketId);

        return secretToken.getSecretAccessKeyHash().equals(accessTokenHash)
                && bucketId.equals(secretToken.getBucketId());
    }
}
