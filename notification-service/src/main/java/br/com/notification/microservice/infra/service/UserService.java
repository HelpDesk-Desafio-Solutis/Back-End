package br.com.notification.microservice.infra.service;

import br.com.notification.microservice.core.app.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();

    public UserResponseDto findById(UUID uuid) {

        return restTemplate.getForObject(
                "http://localhost:8081/users/" + uuid,
                UserResponseDto.class
        );
    }
}