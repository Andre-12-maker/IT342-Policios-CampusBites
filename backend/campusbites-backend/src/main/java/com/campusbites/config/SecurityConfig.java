package com.campusbites.config;

import com.campusbites.auth.service.UserDetailsServiceImpl;
import com.campusbites.common.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity          // enables @PreAuthorize on controllers
public class SecurityConfig {

    private final UserDetailsServiceImpl  userDetailsService;
    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          JwtAuthenticationFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter          = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ── Session: stateless — JWT only, no cookies ─────────────────
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ── CSRF: disabled — stateless REST API uses JWT, not cookies ─
                // If you ever add cookie-based auth, re-enable this.
                .csrf(AbstractHttpConfigurer::disable)

                // ── Security response headers ─────────────────────────────────
                // Prevents clickjacking, MIME sniffing, and leaking the referrer
                .headers(h -> h
                        .frameOptions(f -> f.deny())
                        .contentTypeOptions(c -> {})
                        .referrerPolicy(r ->
                                r.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                )

                // ── Route-level authorisation ─────────────────────────────────
                .authorizeHttpRequests(auth -> auth
                        // Public: auth endpoints (register, login)
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Public: browsing products (GET only — POST/PUT/DELETE require ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/products").permitAll()



                        // Admin-only prefix — belt-and-suspenders on top of @PreAuthorize
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // Everything else requires a valid JWT
                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * BCrypt with cost factor 12.
     * Cost 12 ≈ 250 ms/hash on modern hardware — strong against brute force,
     * still fast enough for login UX.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // SECURITY: hideUserNotFoundExceptions defaults to TRUE in Spring Security —
        // this ensures "user not found" and "bad password" return the same error,
        // preventing email enumeration attacks.
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}