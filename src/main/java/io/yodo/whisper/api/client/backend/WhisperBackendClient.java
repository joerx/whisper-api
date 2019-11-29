package io.yodo.whisper.api.client.backend;

import io.yodo.whisper.api.entity.Shout;
import io.yodo.whisper.api.entity.ShoutPage;

public interface WhisperBackendClient {

    ShoutPage getShouts();

    Shout postShout(Shout shout);

    Shout putShout(int id, Shout shout);
}
