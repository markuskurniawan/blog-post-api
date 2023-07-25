package com.test.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public interface UserDao {

	Optional<User> getUserByToken(String token);
	
}
