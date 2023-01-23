package com.backend.Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.Server.Dto.User;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/auth")
public class login {
	@Autowired
	private com.backend.Server.Repository.User user;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/join")
	public String join(@RequestBody com.backend.Server.Dto.User info)
	{
		log.info("join");
		log.info("pw is : " + info.getPassword());
		info.setPassword(passwordEncoder.encode(info.getPassword()));
		com.backend.Server.Entity.User user_entity = User.toEntity(info);
		user.save(user_entity);
		
		return "join";
	}
	
	@GetMapping("/info")
	public String info()
	{
		log.info("info");
		return "info";
	}
}
