package com.backend.Server.Filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.backend.Server.Entity.User;
import com.backend.Server.Exception.CustomJwtException;
import com.backend.Server.Interface.JwtErrorCode;
import com.backend.Server.Interface.JwtProperties;
import com.backend.Server.Service.JwtService;
import com.backend.Server.UserDetails.PrincipalUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private final JwtService jwtservice;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtservice) {
		super(authenticationManager);
		this.jwtservice = jwtservice;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("Authorization Filter Start");
		
		try {
			jwtservice.checkHeaderValid(request);
			String accessToken = request.getHeader(JwtProperties.HEADER_PREFIX).replace(JwtProperties.TOKEN_PREFIX, "");
			String refreshToken = request.getHeader(JwtProperties.REFRESH_HEADER_PREFIX).replace(JwtProperties.TOKEN_PREFIX, "");
			jwtservice.checkTokenValid(refreshToken);
			
			log.info("리프래쉬 토큰 검증");
			User user = jwtservice.getUserByRefreshToken(refreshToken);
			String username = user.getEmail();
			int id = user.getId();
			
			log.info("리프레쉬 토큰 만료일자 확인후 처리");
			if(jwtservice.IsNeedToUpdateRefreshToken(refreshToken))
			{
				refreshToken = jwtservice.createRefreshToken();
				response.addHeader(JwtProperties.REFRESH_HEADER_PREFIX, JwtProperties.TOKEN_PREFIX + refreshToken);
				jwtservice.setUserByRefreshToken(username, refreshToken);
			}
			
			try {
				log.info("액세스 토큰 검증");
				jwtservice.checkTokenValid(accessToken);
			} catch(TokenExpiredException expired) {
				log.error("액세스토큰 에러 : " + expired.getMessage());
				accessToken = jwtservice.createAccessToken(id, username);
				response.addHeader(JwtProperties.HEADER_PREFIX, JwtProperties.TOKEN_PREFIX + accessToken);
			}
			
			PrincipalUserDetails principalUserDetails = new PrincipalUserDetails(user);
			Authentication authentication = new UsernamePasswordAuthenticationToken(principalUserDetails, null, principalUserDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}catch(CustomJwtException cusJwtExc) {
			log.error("customJwt 에러 : " + cusJwtExc.getMessage());
			request.setAttribute(JwtProperties.EXCEPTION, cusJwtExc.getMessage());
		}catch(TokenExpiredException ee) {
			log.error("TokenExpiredException 에러 : " + ee.getMessage());
			request.setAttribute(JwtProperties.EXCEPTION, JwtErrorCode.JWT_REFRESH_EXPIRED);
		}/*catch(MalformedJwtException | UnsupportedJwtException mj) {
			request.setAttribute(JwtProperties.EXCEPTION, JwtErrorCode.JWT_NOT_VALID);
		}*/
		catch(Exception e) {
			log.error("미정의 에러 : " + e.getMessage());
			request.setAttribute(JwtProperties.EXCEPTION, JwtErrorCode.JWT_NOT_VALID);
		}
		
		chain.doFilter(request, response);
	}

}
