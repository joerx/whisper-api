package io.yodo.whisper.api.client.backend;

import io.yodo.whisper.api.client.Fetch;
import io.yodo.whisper.api.entity.Shout;
import io.yodo.whisper.api.entity.ShoutPage;
import io.yodo.whisper.commons.security.jwt.JWTTokenAuthentication;
import io.yodo.whisper.commons.security.jwt.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
     * @throws IllegalStateException if current authentication is not of type {@link JWTTokenAuthentication}
     */
    private String getToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Found authentication " + auth);

        if (auth instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("No valid authentication found");
        }
        if (!(auth instanceof JWTTokenAuthentication)) {
            throw new IllegalStateException("Invalid authentication found, expected " + JWTTokenAuthentication.class + " but got " + auth.getClass());
        }

        return auth.getCredentials().toString();
    }

    @Override
    public ShoutPage getShouts() {
        log.debug("Requesting shouts from backend");
        String token = getToken();
        return fetch.get("/shouts").auth(token).getResponse(ShoutPage.class);
    }

    @Override
    public Shout postShout(Shout shout) {
        log.debug("Posting shout in backend");
        String token = getToken();
        return fetch.post("/shouts").auth(token).body(shout).getResponse(Shout.class);
    }

    @Override
    public Shout putShout(int id, Shout shout) {
        log.debug("Updating shout " + shout.getId() + " in backend");
        String token = getToken();
        return fetch.put("/shouts/" + id).auth(token).body(shout).getResponse(Shout.class);
    }
}
