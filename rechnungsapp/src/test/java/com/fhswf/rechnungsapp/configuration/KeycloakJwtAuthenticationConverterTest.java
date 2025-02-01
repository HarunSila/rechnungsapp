package com.fhswf.rechnungsapp.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class KeycloakJwtAuthenticationConverterTest {

    private KeycloakJwtAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new KeycloakJwtAuthenticationConverter();
    }

    @Test
    void testConvertJwtWithRoles() {
        Map<String, List<String>> rolesMap = new HashMap<>();
        rolesMap.put("roles", new ArrayList<>(List.of("admin", "user")));

        Map<String, Object> resourceAccess = new HashMap<>();
        resourceAccess.put("rechnungsapp-frontend-client", rolesMap);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("resource_access")).thenReturn(resourceAccess);

        AbstractAuthenticationToken token = converter.convert(jwt);
        assertThat(token).isInstanceOf(JwtAuthenticationToken.class);
        
        Collection<? extends GrantedAuthority> authorities = token.getAuthorities();
        assertThat(authorities)
            .extracting(GrantedAuthority::getAuthority)
            .containsExactlyInAnyOrder("ROLE_admin", "ROLE_user");
    }

    @Test
    void testConvertJwtWithoutRoles() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("resource_access")).thenReturn(Collections.emptyMap());

        AbstractAuthenticationToken token = converter.convert(jwt);
        assertThat(token.getAuthorities()).isEmpty();
    }

    @Test
    void testConvertJwtWithNullResourceAccess() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("resource_access")).thenReturn(null);

        AbstractAuthenticationToken token = converter.convert(jwt);
        assertThat(token.getAuthorities()).isEmpty();
    }
}

