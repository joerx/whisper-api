package io.yodo.whisper.api.controller;

import io.yodo.whisper.api.client.backend.WhisperBackendClient;
import io.yodo.whisper.api.entity.ShoutPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class ShoutController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final WhisperBackendClient backend;

    public ShoutController(WhisperBackendClient backend) {
        this.backend = backend;
    }

    @GetMapping("/shouts")
    public ShoutPage listShouts() throws Exception {
        return backend.getShouts();
    }
}
