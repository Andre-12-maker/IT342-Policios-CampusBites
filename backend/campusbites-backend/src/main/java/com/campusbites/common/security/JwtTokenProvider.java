package com.campusbites.common.security;

import com.campusbites.auth.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Issues and validates JWT tokens.
 *
 * SECURITY hardening applied:
 *  1. Secret must be >= 256 bits (32 bytes Base64-encoded). Enforced at startup
 *     via @PostConstruct — server refuses to start with a weak key.
 *  2. Issuer + audience embedded in every token and verified on every request.
 *     A token issued by another service (or replayed from a different audience)
 *     is rejected even if the signature is valid.
 *  3. validate() throws JwtException on ANY failure — caller (filter) handles.
 *  4. getUserId() is called only after validate() succeeds.
 *
 * How to generate a safe secret:
 *   openssl rand -base64 32
 * Then add to .env:
 *   JWT_SECRET=<output>
 */
@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final String secret;
    private final long   expirationMs;
    private final String issuer;
    private final String audience;

    private SecretKey key;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}")        String secret,
            @Value("${app.jwt.expiration-ms}") long   expirationMs,
            @Value("${app.jwt.issuer}")        String issuer,
            @Value("${app.jwt.audience}")      String audience) {
        this.secret       = secret;
        this.expirationMs = expirationMs;
        this.issuer       = issuer;
        this.audience     = audience;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        if (keyBytes.length < 32) {
            // Fail fast — do not boot with an insecure key
            throw new WeakKeyException(
                    "JWT secret is too short (" + keyBytes.length + " bytes). " +
                            "Minimum is 32 bytes (256 bits). " +
                            "Generate one with: openssl rand -base64 32");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("JWT provider ready — issuer='{}' audience='{}' expiry={}ms",
                issuer, audience, expirationMs);
    }

    // ── Token generation ──────────────────────────────────────────────────────

    public String generate(User user) {
        Date now    = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(user.getId())                        // userId as subject
                .issuer(issuer)                               // who issued this token
                .audience().add(audience).and()               // intended recipient
                .claim("email", user.getEmail())              // convenience claim
                .claim("role",  user.getRole().name())        // used by frontend
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)                                // HS256 via SecretKey
                .compact();
    }

    // ── Token validation ──────────────────────────────────────────────────────

    /**
     * Validates: signature, expiry, issuer, audience.
     * Throws JwtException (subtype) on any failure.
     * The filter catches this and clears the SecurityContext.
     */
    public void validate(String token) {
        buildParser().parseSignedClaims(token);
    }

    public String getUserId(String token) {
        return buildParser().parseSignedClaims(token).getPayload().getSubject();
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private JwtParser buildParser() {
        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(issuer)
                .requireAudience(audience)
                .build();
    }
}