package io.github.LuizMartendal.library.security;

import io.github.LuizMartendal.library.dtos.TokenDto;
import io.github.LuizMartendal.library.enuns.Roles;
import io.github.LuizMartendal.library.exceptions.especifics.BadRequestException;
import io.github.LuizMartendal.library.exceptions.especifics.JwtExpiredException;
import io.github.LuizMartendal.library.services.entities.PersonService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;

@Service
public class JwtTokenProvider {

    private static final String secretKey = "io.github.LuizMartendal";
    private static final long tokenValidation = 3600000;

    @Autowired
    private PersonService personService;

    public TokenDto getToken(String user, Roles role) {
        Date now = new Date();
        Date expiredIn = new Date(now.getTime() + tokenValidation);
        String token = createToken(user, now, expiredIn, role);
        return new TokenDto(user, now, expiredIn, token);
    }

    private String createToken(String user, Date now, Date expiredIn, Roles role) {
        return Jwts
                .builder()
                    .claim("roles", role)
                    .setSubject(user)
                    .setExpiration(expiredIn)
                    .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    public String resolveToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring("Bearer ".length());
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = decodeToken(token);
        UserDetails userDetails = personService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private Claims decodeToken(String token) {
        try {
            return Jwts.
                    parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException("Your session is expired");
        }
    }

    public boolean tokenIsValid(String token) {
        try {
            Claims claims = decodeToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            throw new BadRequestException("Token expired");
        }
    }
}
