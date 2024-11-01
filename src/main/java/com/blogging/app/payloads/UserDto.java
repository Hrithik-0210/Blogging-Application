package com.blogging.app.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	private int id;
	
	@NotEmpty(message = "Name can not be null.")
	@Size(min = 3, message = "username must be minimum of 4 characters")
	private String name;	
	
	@Email(message = "Please enter a valid email id..")
	@NotNull
	private String email;
	
	@NotNull(message = "password can not be null.")
	@Size(min = 4, max = 10, message = "password must be between 4 to 10 characters." )
	private String password;
	
	@NotEmpty(message = "about cant be null...")
	private String about;
	

}
