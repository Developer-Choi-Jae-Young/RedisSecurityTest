package com.backend.Server.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.backend.Server.Entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrincipalUserDetails implements UserDetails{
	private User user;
	
	public PrincipalUserDetails(User user)
	{
		log.info("UserDetails Start");
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		this.user.getRoleList().forEach(R -> {
			authorities.add(()->R);
		});
		return authorities;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public int getId()
	{
		return user.getId();
	}
}
