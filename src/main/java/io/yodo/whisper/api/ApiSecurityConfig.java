package io.yodo.whisper.api;

import io.yodo.whisper.commons.security.jwt.AuthenticationFilter;
import io.yodo.whisper.commons.security.jwt.AuthenticationManager;
import io.yodo.whisper.commons.security.jwt.TokenDecoder;
import io.yodo.whisper.commons.security.jwt.TokenHelper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.*;

@Configuration
@EnableWebSecurity
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    private final RequestMatcher secureURLs = new AndRequestMatcher(
            new AntPathRequestMatcher("/**"),
            new NegatedRequestMatcher(new OrRequestMatcher(
                    new AntPathRequestMatcher("/actuator/**"),
                    new AntPathRequestMatcher("/token")
            ))
    );

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/")
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .exceptionHandling()
            .and()
                .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(secureURLs)
                .authenticated()
            .and()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter(secureURLs);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(TokenDecoder tokenDecoder) {
        return new AuthenticationManager(tokenDecoder);
    }
}
