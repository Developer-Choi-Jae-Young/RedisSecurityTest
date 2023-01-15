package com.backend.Server.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User extends JpaRepository<com.backend.Server.Entity.User, String>{
	
	public Optional<com.backend.Server.Entity.User> findByEmail(String Email);
	public Optional<com.backend.Server.Entity.User> findByRefreshtoken(String token);
}
