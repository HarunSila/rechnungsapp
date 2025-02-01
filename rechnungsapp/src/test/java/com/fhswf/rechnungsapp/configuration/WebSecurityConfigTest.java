package com.fhswf.rechnungsapp.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WebSecurityConfigTest {

    @Mock 
    private JwtDecoder jwtDecoder;

    @Autowired 
    private WebSecurityConfig webSecurityConfig;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testJwtDecoderBeanCreation() {
        assertThat(webSecurityConfig.jwtDecoder()).isNotNull();
    }

    @Test
    void testCorsConfigurationSource() {
        CorsConfigurationSource corsSource = webSecurityConfig.corsConfigurationSource();
        CorsConfiguration config = corsSource.getCorsConfiguration(new MockHttpServletRequest());
        
        assertThat(config).isNotNull();
        if (config != null) {
            assertThat(config.getAllowedOrigins()).containsExactly("http://localhost:4200", "http://frontend:4200");
            assertThat(config.getAllowedMethods()).containsExactly("GET", "POST", "PUT", "DELETE");
            assertThat(config.getAllowedHeaders()).containsExactly("*");
            assertThat(config.getAllowCredentials()).isTrue();
        }
    }

    @Test
    @WithMockUser(roles = "Bill_Creation")
    void testAuthorizedAccessToGetGeschaeftspartner() throws Exception {
        mockMvc.perform(get("/geschaeftspartner"))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "Partner_Administration")
    void testUnauthorizedPostGeschaeftspartner() throws Exception {
        mockMvc.perform(post("/geschaeftspartner"))
               .andExpect(status().isForbidden());
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/geschaeftspartner"))
               .andExpect(status().isUnauthorized());
    }
}

