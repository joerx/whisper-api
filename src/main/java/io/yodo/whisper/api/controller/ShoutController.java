package io.yodo.whisper.api.controller;

import io.yodo.whisper.api.client.backend.WhisperBackendClient;
import io.yodo.whisper.api.entity.ShoutPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoutController {

    private final WhisperBackendClient backend;

    public ShoutController(WhisperBackendClient backend) {
        this.backend = backend;
    }

    @GetMapping("/shouts")
    public ShoutPage listShouts() {
        return backend.getShouts();
    }
}
