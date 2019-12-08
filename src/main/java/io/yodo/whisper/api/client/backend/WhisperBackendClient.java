package io.yodo.whisper.api.client.backend;

import io.yodo.whisper.commons.entity.ShoutDTO;
import io.yodo.whisper.commons.entity.ShoutPageDTO;

public interface WhisperBackendClient {

    ShoutPageDTO getShouts();

    ShoutDTO postShout(ShoutDTO shout);

    ShoutDTO putShout(int id, ShoutDTO shout);

    ShoutDTO getShout(int id);

    ShoutDTO deleteShout(int id);
}
