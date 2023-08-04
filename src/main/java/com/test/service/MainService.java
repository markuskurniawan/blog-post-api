package com.test.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.test.entity.Tbblogpost;
import com.test.repository.Tbblogpostrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.test.entity.TbUser;
import com.test.repository.TbUserRepository;
import com.test.util.JwtTokenUtil;

@Service
public class MainService {
	
	@Autowired
	private TbUserRepository tbUserRepository;

	@Autowired
	private Tbblogpostrepo tbblogpostrepo;
	
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

	public Tbblogpost saveBlogPost(Tbblogpost data){
		return tbblogpostrepo.save(data);
	}

	public List<Tbblogpost> getAllBlogPost(Integer pageNum, Integer pageSize){
		Pageable paging = PageRequest.of(pageNum, pageSize, Sort.by("id").ascending());
		Page<Tbblogpost> data = tbblogpostrepo.findAll(paging);
		return data.getContent();
	}

	public Optional<Tbblogpost> findBlogPostById(Integer id){
		return tbblogpostrepo.findById(id);
	}

	public Map<String, Object> updateBlogPost(Tbblogpost data){
		Map<String, Object> message = new HashMap<>();
		Optional<Tbblogpost> checkData = tbblogpostrepo.findById(data.getId());
		if (checkData.isPresent()){
			Tbblogpost dataUpdated = tbblogpostrepo.save(data);
			message.put("rspnCd", 1);
			message.put("rspnMsg", "update success");
			message.put("data", dataUpdated);
		} else {
			message.put("rspnCd", 0);
			message.put("rspnMsg", "data not found");
		}
		return message;
	}

	public void deleteById(Integer id){
		tbblogpostrepo.deleteById(id);
	}
}
