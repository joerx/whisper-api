package io.yodo.whisper.api.client.backend;

import io.yodo.whisper.api.entity.ShoutPage;

public interface WhisperBackendClient {

    ShoutPage getShouts();
}
