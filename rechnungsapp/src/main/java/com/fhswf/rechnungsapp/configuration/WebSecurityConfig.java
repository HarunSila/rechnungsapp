package com.fhswf.rechnungsapp.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Diese Klasse konfiguriert die Sicherheitseinstellungen der Anwendung. 
 * Hier wird festgelegt, welche Endpunkte für welche Rollen zugänglich sind.
 * Auch werden hier die JWT-Decoder und der JWT-Authentifizierungskonverter konfiguriert.
 * 
 * CORS (Cross-Origin Resource Sharing) wird ebenfalls konfiguriert, um Anfragen von der Angular-App zur Entwicklungszeit zu erlauben.
 * 
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${spring.security.oauth2.client.provider.rechnungsapp.issuer-uri}")
    private String issuerUri;

    // Konfiguration des Keycloak JWT-Authentifizierungskonverters und des JWT-Decoders
    @Bean
    KeycloakJwtAuthenticationConverter  keycloakJwtAuthenticationConverter() {
        return new KeycloakJwtAuthenticationConverter();
    }
    @Bean
    JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri); // Automatically discovers JWKS URL based on the issuer
    }

    // CORS configuration to allow requests from Angular app
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://frontend:4200")); // Allow Angular app
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Allow HTTP methods
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies/auth headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }

    // Security filter chain configuration to define which endpoints are accessible by which roles
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Add CORS configuration
            .authorizeHttpRequests(authorize -> authorize
                // Allow access to these endpoints without authentication (for login page, assets, etc.)
                .requestMatchers("/", "/login", "/index.html", "/assets/**", "/static/**", "/favicon.ico", "/main.js", "/polyfills.js").permitAll()

                // Define which roles are allowed to access which endpoints
                .requestMatchers(HttpMethod.GET, "/api/geschaeftspartner").hasRole("Bill_Creation")
                .requestMatchers(HttpMethod.GET, "/api/geschaeftspartner/{id}").hasRole("Bill_Creation")
                .requestMatchers(HttpMethod.POST, "/api/geschaeftspartner").hasRole("Bill_Creation")
                .requestMatchers(HttpMethod.POST, "/api/rechnung").hasRole("Bill_Creation")

                .requestMatchers(HttpMethod.PUT, "/api/geschaeftspartner/{id}").hasRole("Partner_Administration")
                .requestMatchers(HttpMethod.DELETE, "/api/geschaeftspartner/{id}").hasRole("Partner_Administration")

                .anyRequest().authenticated()) // Require authentication for all other requests
            .oauth2Login((oauth2Login) -> oauth2Login.permitAll()) // Allow access to OAuth2 login page

            // Configure OAuth2 resource server to use JWT authentication and the Keycloak JWT converter
            .oauth2ResourceServer(server -> server
                .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter())));
        
        return http.build();
    }
}
