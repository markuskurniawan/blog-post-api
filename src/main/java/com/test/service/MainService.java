package com.test.service;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.entity.TbUser;
import com.test.repository.TbUserRepository;
import com.test.util.JwtTokenUtil;

@Service
public class MainService {
	
	@Autowired
	private TbUserRepository tbUserRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public TbUser login(TbUser user) throws ParseException {
		boolean match = false;
		Optional<TbUser> readUser = tbUserRepository.findById(user.getUsername());
		if (readUser.isPresent()) {
			
			boolean validatePassword = user.getPassword().equalsIgnoreCase(readUser.get().getPassword());
			
			if(validatePassword == true) {
				match = true;
			}
			
			if (match) {
				user = new TbUser();
				user = readUser.get();
				String token = jwtTokenUtil.doGenerateToken(user); // get token
				user.setToken(token);
				tbUserRepository.save(user);
				user.setResponseMessage("Success");
				user.setResponseCode("1");
			} else {
				user.setResponseMessage("Invalid password");
				user.setResponseCode("0");
			}
		} else {
			user.setResponseMessage("User Not Found");
			user.setResponseCode("0");
		}
		return user;
	}
}
