package com.test.repository;

import org.springframework.data.repository.CrudRepository;

import com.test.entity.TbUser;

public interface TbUserRepository extends CrudRepository<TbUser, String> {

}
