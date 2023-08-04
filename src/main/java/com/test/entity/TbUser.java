package com.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbuser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class TbUser {

	@Id
	@Column(name = "username")
	String username;
	
	@Column(name = "password")
	String password;
	
	@Column(name = "token")
	String token;
	
	@Transient
	String responseCode;
	
	@Transient
	String responseMessage;

	public TbUser(String username, String password, String token) {
		this.username = username;
		this.password = password;
		this.token = token;
	}
}
