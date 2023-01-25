package com.backend.Server.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.backend.Server.Interface.JwtProperties;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CorsConfig {
	@Bean
    public CorsFilter corsFilter() {
		log.info("Cors Filter is Start !!!");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        //config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader(JwtProperties.HEADER_PREFIX);
        config.addExposedHeader(JwtProperties.REFRESH_HEADER_PREFIX);
        source.registerCorsConfiguration("/**", config);    	
        return new CorsFilter(source);
	}
}
