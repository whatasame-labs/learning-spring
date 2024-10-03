package com.github.whatasame.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.whatasame.security.filter.LoginWithEmailFilter;
import com.github.whatasame.security.handler.LoginWithEmailFailureHandler;
import com.github.whatasame.security.handler.LoginWithEmailSuccessHandler;
import com.github.whatasame.security.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http,
            final AuthenticationConfiguration authenticationConfiguration,
            final ObjectMapper objectMapper,
            final JwtProvider jwtProvider)
            throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(it -> it.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        new LoginWithEmailFilter(
                                request -> request.getRequestURI().equals("/login"),
                                authenticationManager(authenticationConfiguration),
                                new LoginWithEmailSuccessHandler(objectMapper, jwtProvider),
                                new LoginWithEmailFailureHandler(objectMapper),
                                objectMapper),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(it -> it.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
