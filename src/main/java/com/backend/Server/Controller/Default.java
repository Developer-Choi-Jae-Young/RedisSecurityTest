package com.backend.Server.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class Default {
	@GetMapping("/home")
	private String home() {
		log.info("home");
		return "home";
	}
}
