package com.whatasame.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatasame.security.model.LoginWithEmail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class LoginWithEmailFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public LoginWithEmailFilter(
            final RequestMatcher requiresAuthenticationRequestMatcher,
            final AuthenticationManager authenticationManager,
            final AuthenticationSuccessHandler successHandler,
            final AuthenticationFailureHandler failureHandler,
            final ObjectMapper objectMapper) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);

        this.objectMapper = objectMapper; // 역직렬화에 사용
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        final LoginWithEmail body = objectMapper.readValue(request.getInputStream(), LoginWithEmail.class);

        final UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(body.email(), body.password());

        return getAuthenticationManager().authenticate(authentication);
    }
}
