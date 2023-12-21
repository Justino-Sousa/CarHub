package br.com.js.carhub.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.js.carhub.model.User;

@Service
public class TokenService {
	
	public final String secret = "uma_secret_bem_segura";

	public String generateToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create()
					.withIssuer("auth-api")
					.withSubject(user.getLogin())
					.withExpiresAt(genExpirationDate())
					.sign(algorithm);
			return token;
		} catch (JWTCreationException e) {
			throw new RuntimeException("Error while generating token", e);
		}	
	}
	
	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			 return JWT.require(algorithm)
					.withIssuer("auth-api")
					.build()
					.verify(token)
					.getSubject();
		} catch (JWTVerificationException e) {
			return "token-expired";
		}	
	}
	
	private Instant genExpirationDate() {
		return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00")); 
	}
	
}
