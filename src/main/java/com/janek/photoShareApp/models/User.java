package com.janek.photoShareApp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
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

	@NotNull
	@Size(min = 3, max = 20)
	private String username;

	@NotNull
	@Size(max = 50)
	@Email
	private String email;

	@NotNull
	@Size(min = 6, max = 40)
	private String password;

	@Size(max = 20)
	private String name = "None";

	@Enumerated(EnumType.STRING)
	private ERole role;

	public User() {
	}

	public User(String username, String email, String password, ERole role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}
}
