package io.yodo.whisper.api.client.backend;

import io.yodo.whisper.api.client.Fetch;
import io.yodo.whisper.api.entity.TokenRequest;
import io.yodo.whisper.api.entity.TokenResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class WhisperAuthHttpClient implements WhisperAuthClient{

    private final Fetch fetch;

    public WhisperAuthHttpClient(@Qualifier("authClient") Fetch fetch) {
        this.fetch = fetch;
    }

    @Override
    public TokenResponse getToken(TokenRequest req) {
        return fetch.post("/token").body(req).getResponse(TokenResponse.class);
    }
}
