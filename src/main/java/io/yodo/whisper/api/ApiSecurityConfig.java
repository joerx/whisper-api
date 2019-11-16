package io.yodo.whisper.api;

import io.yodo.whisper.commons.security.jwt.JWTAuthenticationFilter;
import io.yodo.whisper.commons.security.jwt.JWTAuthenticationManager;
import io.yodo.whisper.commons.security.jwt.JWTTokenHelper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.*;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JWTTokenHelper.class)
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
    public JWTAuthenticationFilter authenticationFilter() throws Exception {
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(secureURLs);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public JWTTokenHelper tokenHelper() {
        return new JWTTokenHelper();
    }

    @Bean
    public AuthenticationManager authenticationManager(JWTTokenHelper tokenHelper) {
        return new JWTAuthenticationManager(tokenHelper);
    }
}
