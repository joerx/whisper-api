package io.yodo.whisper.api.controller;

import io.yodo.whisper.api.client.backend.WhisperBackendClient;
import io.yodo.whisper.commons.entity.ShoutDTO;
import io.yodo.whisper.commons.entity.ShoutPageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShoutController {

    private final WhisperBackendClient backend;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public ShoutController(WhisperBackendClient backend) {
        this.backend = backend;
    }

    @GetMapping("/shouts")
    public ShoutPageDTO listShouts() {
        return backend.getShouts();
    }

    @GetMapping("/shouts/{id}")
    public ShoutDTO getShout(@PathVariable int id) {
        return backend.getShout(id);
    }

    @PostMapping("/shouts")
    public ShoutDTO createShout(@RequestBody ShoutDTO shout) {
        log.debug("Posting shout " + shout);
        return backend.postShout(shout);
    }

    @PutMapping("/shouts/{id}")
    public ShoutDTO updateShout(@PathVariable int id, @RequestBody ShoutDTO shout) {
        log.debug("Updating shout " + shout);
        return backend.putShout(id, shout);
    }

    @DeleteMapping("/shouts/{id}")
    public ShoutDTO deleteShout(@PathVariable int id) {
        return backend.deleteShout(id);
    }
}
