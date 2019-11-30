package io.yodo.whisper.api;

import io.yodo.whisper.commons.security.config.JWTConfigAdapter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("security.jwt")
public class JWTConfig extends JWTConfigAdapter {
}
