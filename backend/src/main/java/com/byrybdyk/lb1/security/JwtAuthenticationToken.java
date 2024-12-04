package com.byrybdyk.lb1.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Jwt jwt;
    private final Object principal;

    public JwtAuthenticationToken(Jwt jwt, Collection<GrantedAuthority> authorities) {
        super(authorities);
        this.jwt = jwt;
        this.principal = jwt.getSubject();
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public Jwt getJwt() {
        return jwt;
    }
}
