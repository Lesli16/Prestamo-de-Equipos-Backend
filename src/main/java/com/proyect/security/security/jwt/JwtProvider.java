package com.proyect.security.security.jwt;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class JwtProvider {
    //Logger para mostrar los errores
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    //Clave para verificar el token
    @Value("${jwt.secret}")
    private String secret;

    //Tiempo base de expiración
    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication) {

        UserDetails mainUser = (UserDetails) authentication.getPrincipal();
        String role = mainUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get();
        return Jwts.builder().setSubject(mainUser.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //Creamos una función que permita obtener el nombre de usuario con el token
    public String getUserNameFromToken(String token) {

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String getRoleFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        String role = (String) claims.get("role");
        System.out.println(username + " - Role: " + role);
        return role;
    }

    //Creamos una función que permita validar nuestro token con la firma secreta
    //Controlamos cualquier error que pueda existir con el token

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("token mal formado");
        } catch (UnsupportedJwtException e) {
            logger.error("token no soportado");
        } catch (ExpiredJwtException e) {
            logger.error("token expirado");
        } catch (IllegalArgumentException e) {
            logger.error("token vacío");
        } catch (SignatureException e) {
            logger.error("fail en la firma");
        }
        return false;
    }
}
