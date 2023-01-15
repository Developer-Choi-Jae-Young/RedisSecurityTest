package com.backend.Server.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.Server.Exception.BussinessException;
import com.backend.Server.Interface.ExMessage;
import com.backend.Server.Repository.User;
import com.backend.Server.UserDetails.PrincipalUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalUserDetailsService implements UserDetailsService{
	private final User user;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("UserDetailsService Start");
		com.backend.Server.Entity.User user_entity =  user.findByEmail(username).orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		return new PrincipalUserDetails(user_entity);
	}

}
