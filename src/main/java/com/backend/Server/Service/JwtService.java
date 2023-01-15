package com.backend.Server.Service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.backend.Server.Exception.CustomJwtException;
import com.backend.Server.Interface.JwtErrorCode;
import com.backend.Server.Interface.JwtProperties;
import com.backend.Server.Repository.User;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {
	private String SECRET_KEY = "JwtSecretValueIsVeryImportantSoThisValueIsVeryLong";
	private final User user;
	
	@Transactional(readOnly = true)
	public com.backend.Server.Entity.User getUserByRefreshToken(String token)
	{
		return user.findByRefreshtoken(token).orElseThrow(() -> new CustomJwtException(JwtErrorCode.JWT_REFRESH_EXPIRED));
	}
	
	@Transactional
	public void setUserByRefreshToken(String username, String refreshtoken)
	{
		user.findByEmail(username).ifPresent(user -> user.setRefreshtoken(refreshtoken));
	}
	
	@Transactional
	public void removeRefreshToken(String token) 
	{
		user.findByRefreshtoken(token).ifPresent(user -> user.setRefreshtoken(null));
	}
	
	public void logout(HttpServletRequest req)
	{
		try {
			checkHeaderVaild(req);
			String refresh =req.getHeader(JwtProperties.REFRESH_HEADER_PREFIX).replace(JwtProperties.TOKEN_PREFIX, "");
			removeRefreshToken(refresh);
		}catch(Exception e) {
			throw new CustomJwtException(JwtErrorCode.JWT_ACCESS_NOT_VALID);
		}
	}
	
	public String createAccessToken(int id, String username)
	{
		return JWT.create().withSubject(JwtProperties.ACCESS_TOKEN)
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.withClaim(JwtProperties.ID, id)
				.withClaim(JwtProperties.USERNAME, username)
				.sign(Algorithm.HMAC512(SECRET_KEY));
	}
	
	public String createRefreshToken()
	{
		return JWT.create().withSubject(JwtProperties.REFRESH_TOKEN)
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRETSH_EXPIRATION_TIME)).sign(Algorithm.HMAC512(SECRET_KEY));
	}
	
	public void checkHeaderVaild(HttpServletRequest req)
	{
		String access = req.getHeader(JwtProperties.HEADER_PREFIX);
		String refresh = req.getHeader(JwtProperties.REFRESH_HEADER_PREFIX);
		
		if(access == null)
		{
			throw new CustomJwtException(JwtErrorCode.JWT_ACCESS_NOT_VALID);
		}else if(refresh == null)
		{
			throw new CustomJwtException(JwtErrorCode.JWT_REFRESH_NOT_VALID);
		}
	}
	
	public void checkTokenValid(String token)
	{
		JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
	}
	
	public boolean IsExpiredToken(String token)
	{
		try {
			JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
		}catch(TokenExpiredException expired) {
			return false;
		}
		return false;
	}
	
	public boolean IsNeedToUpdateRefreshToken(String token)
	{
		try {
			Date expiredAt = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token).getExpiresAt();
			
			Date current = new Date(System.currentTimeMillis());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(current);
			calendar.add(Calendar.DATE, 7);
			
			Date after7dayFromToday = calendar.getTime();
			if(expiredAt.before(after7dayFromToday)) {
				return true;
			}
			
		}catch(TokenExpiredException expired) {
			return true;
		}
		return false;
	}
}
