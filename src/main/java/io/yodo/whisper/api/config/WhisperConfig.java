package io.yodo.whisper.api.config;

import io.yodo.whisper.api.http.Fetch;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("whisper")
public class WhisperConfig {

    private HttpClientConfig backend;

    private HttpClientConfig auth;

    @Bean(destroyMethod = "close")
    public Fetch backendClient() {
        return new Fetch(backend.getUrl());
    }

    @Bean(destroyMethod = "close")
    public Fetch authClient() {
        return new Fetch(auth.getUrl());
    }

    public HttpClientConfig getBackend() {
        return backend;
    }

    public void setBackend(HttpClientConfig backend) {
        this.backend = backend;
    }

    public HttpClientConfig getAuth() {
        return auth;
    }

    public void setAuth(HttpClientConfig auth) {
        this.auth = auth;
    }
}
