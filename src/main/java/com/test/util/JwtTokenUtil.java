package com.test.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.test.entity.TbUser;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${token.exp.min}")
	private String tokenExpMin;
	
	public String doGenerateToken(TbUser user) {
		return generateToken(user.getUsername());
	}
	
	private String generateToken(String subject) {
		long JWT_TOKEN_VALIDITY = Long.valueOf(tokenExpMin) * 60;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		JwtBuilder builder = Jwts.builder().setSubject(subject).setIssuedAt(now)
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret);
		return builder.compact();
	}
	
	public String getSubjectToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public Date expiredToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
	}
	
	public Boolean isTokenExpired(String token) {
		Date expiration =  expiredToken(token);
		return expiration.before(new Date());
	}
	
	public Boolean validateToken(String token, TbUser user) {
		String username = getSubjectToken(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token));
	}

}
