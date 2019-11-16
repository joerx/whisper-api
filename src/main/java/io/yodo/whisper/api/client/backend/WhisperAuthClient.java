package io.yodo.whisper.api.client.backend;

import io.yodo.whisper.api.entity.TokenRequest;
import io.yodo.whisper.api.entity.TokenResponse;

public interface WhisperAuthClient {

    TokenResponse getToken(TokenRequest req);
}
