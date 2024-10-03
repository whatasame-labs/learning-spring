package com.github.whatasame.security.provider;

import com.github.whatasame.security.model.Member;
import com.github.whatasame.security.repository.InMemoryMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginWithEmailAuthenticationProvider implements AuthenticationProvider {

    private final InMemoryMemberRepository inMemoryMemberRepository;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String email = authentication.getName();
        final String password = authentication.getCredentials().toString();

        final Member member = inMemoryMemberRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No member found with the provided email."));

        if (!member.password().equals(password)) {
            throw new BadCredentialsException("The password does not match.");
        }

        final List<SimpleGrantedAuthority> grantedAuthorities =
                member.roles().stream().map(SimpleGrantedAuthority::new).toList();

        return new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities);
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
