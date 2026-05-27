package com.campusbites.common.security;

import com.campusbites.auth.service.UserDetailsServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Stateless JWT authentication filter — runs once per request.
 *
 * SECURITY rules:
 *  1. Any token validation failure clears the SecurityContext — no partial auth.
 *  2. JWT error details are logged server-side ONLY — never sent to client.
 *  3. A deleted user whose token is still valid is rejected immediately.
 *  4. Only sets auth if validate() passes fully (sig + expiry + issuer + audience).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider       jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider  = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest  request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain         chain)
            throws ServletException, IOException {

        String token = extractBearerToken(request);

        if (StringUtils.hasText(token)) {
            try {
                jwtTokenProvider.validate(token);

                String      userId      = jwtTokenProvider.getUserId(token);
                UserDetails userDetails = userDetailsService.loadUserById(userId);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException ex) {
                // Expired, tampered, wrong issuer/audience — treat as anonymous
                log.warn("JWT rejected for [{}] — {}: {}",
                        request.getRequestURI(),
                        ex.getClass().getSimpleName(),
                        ex.getMessage());
                SecurityContextHolder.clearContext();

            } catch (UsernameNotFoundException ex) {
                // Token was valid but the user was deleted after it was issued
                log.warn("JWT references deleted user — rejecting");
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private String extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7).strip(); // strip() removes accidental whitespace
        }
        return null;
    }
}