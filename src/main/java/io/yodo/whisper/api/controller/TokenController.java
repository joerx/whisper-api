package io.yodo.whisper.api.controller;

import io.yodo.whisper.api.client.backend.WhisperAuthClient;
import io.yodo.whisper.api.entity.TokenRequest;
import io.yodo.whisper.api.entity.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final WhisperAuthClient authClient;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public TokenController(WhisperAuthClient authClient) {
        this.authClient = authClient;
    }

    @PostMapping("/token")
    public TokenResponse getToken(@RequestBody TokenRequest tokenRequest) {
        log.debug("Requesting token from auth service");
        return authClient.getToken(tokenRequest);
    }
}
