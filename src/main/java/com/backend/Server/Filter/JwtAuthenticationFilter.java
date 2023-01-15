package com.backend.Server.Filter;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.backend.Server.Interface.JwtProperties;
import com.backend.Server.Service.JwtService;
import com.backend.Server.UserDetails.PrincipalUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtservice;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("Authentication Filter Start");
		
		try {	
			ObjectMapper om = new ObjectMapper();
			com.backend.Server.Entity.User user = om.readValue(request.getInputStream(), com.backend.Server.Entity.User.class);
			log.info(user.getEmail());
			log.info(user.getPassword());
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
			Authentication authentication =  authenticationManager.authenticate(authenticationToken);
			
			PrincipalUserDetails details =  (PrincipalUserDetails)authentication.getPrincipal();
			log.info(details.getUsername());
			
			return authentication;
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("Athentication is Success");
		
		PrincipalUserDetails principalUserDetails = (PrincipalUserDetails)authResult.getPrincipal();
		int id = principalUserDetails.getId();
		String username = principalUserDetails.getUsername();
		String access = jwtservice.createAccessToken(id, username);
		String refresh = jwtservice.createRefreshToken();
		
		jwtservice.setUserByRefreshToken(username, refresh);
		
		response.addHeader(JwtProperties.HEADER_PREFIX, JwtProperties.TOKEN_PREFIX + access);
		response.addHeader(JwtProperties.REFRESH_HEADER_PREFIX, JwtProperties.TOKEN_PREFIX + refresh);
		setSuccessResponse(response, "로그인 성공");
		//super.successfulAuthentication(request, response, chain, authResult);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.info("Athentication is UnSuccess");
		
		String failReason = "";//failed.getMessage().equals(ExMessage.MEMBER_ERROR_NOT_FOUND_ENG.getMessage()) ? ExMessage().MEMBER_ERROR_NOT_FOUND.getMessage() : ExMessage.MEMBER_ERROR_PASSWORD.getMessage();
		setFailResponse(response, failReason);
		//super.unsuccessfulAuthentication(request, response, failed);
	}
	
	private void setSuccessResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json;charset=UTF-8");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);
		jsonObject.put("code", 1);
		jsonObject.put("message", message);
		
		response.getWriter().print(jsonObject);
	}
	
	private void setFailResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("application/json;charset=UTF-8");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", false);
		jsonObject.put("code", -1);
		jsonObject.put("message", message);
		
		response.getWriter().print(jsonObject);
	}
	
}
