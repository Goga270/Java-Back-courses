package com.example.experienceexchange.security.provider;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.exception.JwtTokenInvalidException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider implements IJwtTokenProvider {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_INVALID = "Jwt token is invalid";

    private final byte[] SECRET;
    private final Long validityInMillisSeconds;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(@Value("${jwt.token.secret}") String secret,
                            @Value("${jwt.token.ExpirationTime}") Long validityInMillisSeconds,
                            UserDetailsService userDetailsService) {

        this.SECRET = TextCodec.BASE64.decode(secret);

        this.validityInMillisSeconds = validityInMillisSeconds;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String createToken(String email, Role role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", String.valueOf(role));

        Date now = new Date();

        Date validity = new Date(now.getTime() + validityInMillisSeconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails, userDetails.getAuthorities());
    }

    @Override
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public Boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtTokenInvalidException(TOKEN_INVALID);
        }
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HEADER_AUTHORIZATION);
    }
}
