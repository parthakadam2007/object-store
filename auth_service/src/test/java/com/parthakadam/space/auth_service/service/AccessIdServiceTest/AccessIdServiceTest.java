package com.parthakadam.space.auth_service.service.AccessIdServiceTest;

import com.parthakadam.space.auth_service.repositorys.SecretTokenRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccessIdServiceTest {

    @Mock
    private SecretTokenRepository secretTokenRepository;

    @InjectMocks
    private AccessIdServiceTest accessIdServiceTest;
}
