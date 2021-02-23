package com.example.aviasale.security;

import com.example.aviasale.domain.entity.Role;
import com.example.aviasale.expection.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final String signatureKey = "Victor";
    private final String authHeader = "Authorization";
    private final long accessTokenValidity = 1800 * 1000;
    private final long refreshTokenValidity = 604800 * 1000;

    public JwtTokenProvider(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateAccessToken(String email, Collection<Role> role) {
        return generateToken(email, role, accessTokenValidity, false);
    }

    public String generateRefreshToken(String email, Collection<Role> role) {
        return generateToken(email, role, refreshTokenValidity, true);
    }

    private String generateToken(String email, Collection<Role> role, long validityInMilliseconds, boolean isRefresh) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("refresh", isRefresh);
        claims.put("roles", role.stream().collect(Collectors.toMap(Role::getRoleId, Role::getRoleName)));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, signatureKey)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token);

            if (claimsJws.getBody().get("refresh", Boolean.class)) {
                return false;
            }
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new JwtAuthenticationException("Access token is expired", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token);
            if (claimsJws.getBody().get("refresh", Boolean.class)) {
                return !claimsJws.getBody().getExpiration().before(new Date());
            }
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new JwtAuthenticationException("Refresh token is expired", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmailFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(authHeader);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
