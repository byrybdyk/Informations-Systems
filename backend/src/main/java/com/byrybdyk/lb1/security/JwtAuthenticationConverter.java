package com.byrybdyk.lb1.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collection;

public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final Converter<Jwt, Collection<GrantedAuthority>> grantedAuthoritiesConverter;

    public JwtAuthenticationConverter(Converter<Jwt, Collection<GrantedAuthority>> grantedAuthoritiesConverter) {
        this.grantedAuthoritiesConverter = grantedAuthoritiesConverter;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);

        return new JwtAuthenticationToken(jwt, authorities);
    }
}
