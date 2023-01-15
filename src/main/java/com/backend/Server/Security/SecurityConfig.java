package com.backend.Server.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.backend.Server.Filter.JwtAuthenticationFilter;
import com.backend.Server.Filter.JwtAuthorizationFilter;
import com.backend.Server.Repository.User;
import com.backend.Server.Service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
	private final User user;
	private final JwtService jwt;
	
	private static final String[] AUTH_WHITELIST = {
            "/auth/**"
    };
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		log.info("security start");
		
		AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
		 return http
	                .authorizeHttpRequests(authorize -> authorize
	                        .shouldFilterAllDispatcherTypes(false)
	                        .requestMatchers(AUTH_WHITELIST)
	                        .permitAll()
	                        .anyRequest()
	                        .authenticated()).csrf().disable().formLogin().disable().httpBasic().disable()
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwt))
	                .addFilter(new JwtAuthorizationFilter(authenticationManager, jwt))
	                .build();
	}
	
	@Bean
	protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
