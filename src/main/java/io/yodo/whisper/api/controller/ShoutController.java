package io.yodo.whisper.api.controller;

import io.yodo.whisper.api.client.backend.WhisperBackendClient;
import io.yodo.whisper.api.entity.Shout;
import io.yodo.whisper.api.entity.ShoutPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoutController {

    private final WhisperBackendClient backend;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public ShoutController(WhisperBackendClient backend) {
        this.backend = backend;
    }

    @GetMapping("/shouts")
    public ShoutPage listShouts() {
        return backend.getShouts();
    }

    @PostMapping("/shouts")
    public Shout createShout(@RequestBody Shout shout) {
        log.debug("Posting shout " + shout);
        return backend.postShout(shout);
    }
}
