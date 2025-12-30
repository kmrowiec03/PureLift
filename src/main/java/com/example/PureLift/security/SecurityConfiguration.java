package com.example.PureLift.security;



import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Component
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/api/security/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/metrics/populate-test-data").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/metrics/create-test-users").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/coaches/{coachId}/join").hasAnyRole("USER", "ADMIN", "COACH")
                        .requestMatchers(HttpMethod.POST,"/api/coaches/{coachId}/request").hasAnyRole("USER", "ADMIN", "COACH")
                        .requestMatchers(HttpMethod.GET,"/api/coaches/requests").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.POST,"/api/coaches/requests/{requestId}/accept").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.POST,"/api/coaches/requests/{requestId}/reject").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.GET,"/api/coaches/**").hasAnyRole("USER", "ADMIN", "COACH")
                        .requestMatchers(HttpMethod.POST,"/api/coaches/{coachId}/clients").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers(HttpMethod.DELETE,"/api/coaches/**").hasAnyRole("ADMIN", "COACH")
                        .requestMatchers("/api/training/**").hasAnyRole("USER", "ADMIN", "COACH")
                        .requestMatchers("/api/metrics/**").hasAnyRole("USER", "ADMIN", "COACH")


                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("USER", "ADMIN", "COACH")
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)  // ← użyj pola, nie parametru
                .build();
    }

}
