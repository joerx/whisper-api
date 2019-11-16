package io.yodo.whisper.api.controller;

import io.yodo.whisper.api.client.backend.WhisperAuthClient;
import io.yodo.whisper.api.entity.TokenRequest;
import io.yodo.whisper.api.entity.TokenResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final WhisperAuthClient authClient;

    public TokenController(WhisperAuthClient authClient) {
        this.authClient = authClient;
    }

    @PostMapping("/token")
    public TokenResponse getToken(@RequestBody TokenRequest tokenRequest) {
        return authClient.getToken(tokenRequest);
    }
}
