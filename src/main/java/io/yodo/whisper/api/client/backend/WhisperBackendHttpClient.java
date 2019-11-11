package io.yodo.whisper.api.client.backend;

import io.yodo.whisper.api.client.Fetch;
import io.yodo.whisper.api.entity.ShoutPage;
import io.yodo.whisper.api.entity.TokenRequest;
import io.yodo.whisper.api.entity.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public class WhisperBackendHttpClient implements WhisperBackendClient{

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Fetch fetch;

    private final WhisperBackendConfig config;

    public WhisperBackendHttpClient(WhisperBackendConfig config) {
        this.log.debug("Backend url: " + config.getUrl());
        this.fetch = new Fetch(config);
        this.config = config;
    }

    private String getToken() {
        // TODO: buffer tokens, only request new one if existing one is not valid anymore
        log.debug("Requesting token from backend");
        log.debug("Using client id " + config.getClientId());

        TokenRequest req = new TokenRequest(config.getClientId(), config.getClientSecret());
        TokenResponse tr = fetch.post("/token").body(req).getResponse(TokenResponse.class);

        return tr.getToken();
    }

    @Override
    public ShoutPage getShouts() {
        log.debug("Requesting shouts from backend");
        String token = getToken();
        return fetch.get("/shouts").auth(token).getResponse(ShoutPage.class);
    }

    @PreDestroy
    public void close() throws IOException {
        fetch.close();
    }
}
