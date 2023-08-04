package com.test;

import com.test.entity.TbUser;
import com.test.repository.TbUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication implements CommandLineRunner {

	private final TbUserRepository tbUserRepository;

	@Autowired
	public TestApplication(TbUserRepository tbUserRepository) {
		this.tbUserRepository = tbUserRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		TbUser user = new TbUser("admin", "admin", "");
		tbUserRepository.save(user);
	}
}
