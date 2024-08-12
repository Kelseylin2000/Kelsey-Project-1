package com.example.stylish.security;

import com.example.stylish.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Method to generate JWT token
    public String generateToken(UserDto userDto, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDto.getId());
        claims.put("provider", userDto.getProvider());
        claims.put("name", userDto.getName());
        claims.put("email", userDto.getEmail());
        claims.put("picture", userDto.getPicture());
        claims.put("roles", roles);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDto.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    // Method to parse all claims from the token
    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    // Method to extract user information from the token
    public UserDto getUserDtoFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        return new UserDto(
                ((Number) claims.get("id")).intValue(),
                (String) claims.get("provider"),
                (String) claims.get("name"),
                (String) claims.get("email"),
                (String) claims.get("picture")
        );
    }

    // Method to extract roles from the token
    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        return (List<String>) claims.get("roles");
    }

    // Method to validate the token
    public boolean validateToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims != null && !isTokenExpired(claims);
    }

    // Method to check if the token is expired
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    // Method to resolve the token from the HTTP request
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}