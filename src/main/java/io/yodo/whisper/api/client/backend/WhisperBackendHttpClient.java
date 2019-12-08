package io.yodo.whisper.api.client.backend;

import io.yodo.whisper.api.http.Fetch;
import io.yodo.whisper.commons.entity.ShoutDTO;
import io.yodo.whisper.commons.entity.ShoutPageDTO;
import io.yodo.whisper.commons.security.jwt.TokenAuthentication;
import io.yodo.whisper.commons.security.jwt.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WhisperBackendHttpClient implements WhisperBackendClient{

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Fetch fetch;

    public WhisperBackendHttpClient(@Qualifier("backendClient") Fetch fetch) {
        this.fetch = fetch;
    }

    /**
     * Get token from current authentication context
     * @return current authentication token
     * @throws UnauthorizedException if no valid authentication is found
     */
    private String getToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Found authentication " + auth);
        if (!(auth instanceof TokenAuthentication)) {
            throw new UnauthorizedException("No valid authentication found");
        }
        return ((TokenAuthentication) auth).getToken();
    }

    @Override
    public ShoutPageDTO getShouts() {
        log.debug("Requesting shouts from backend");
        String token = getToken();
        return fetch.get("/shouts").auth(token).getResponse(ShoutPageDTO.class);
    }

    @Override
    public ShoutDTO postShout(ShoutDTO shout) {
        log.debug("Posting shout in backend");
        String token = getToken();
        return fetch.post("/shouts").auth(token).body(shout).getResponse(ShoutDTO.class);
    }

    @Override
    public ShoutDTO putShout(int id, ShoutDTO shout) {
        log.debug("Updating shout " + shout.getId() + " in backend");
        String token = getToken();
        return fetch.put("/shouts/" + id).auth(token).body(shout).getResponse(ShoutDTO.class);
    }

    @Override
    public ShoutDTO getShout(int id) {
        String token = getToken();
        return fetch.get("/shouts/" + id).auth(token).getResponse(ShoutDTO.class);
    }

    @Override
    public ShoutDTO deleteShout(int id) {
        String token = getToken();
        return fetch.delete("/shouts/" + id).auth(token).getResponse(ShoutDTO.class);
    }
}
