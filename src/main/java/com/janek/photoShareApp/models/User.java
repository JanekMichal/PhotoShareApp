package com.janek.photoShareApp.models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
@Setter
@Getter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@Size(max = 20)
	private String name = "None";


	
	@Enumerated(EnumType.STRING)
	private ERole role = ERole.ROLE_USER;

	public User() {
	}

	public User(String username, String email, String password, ERole role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}
}
