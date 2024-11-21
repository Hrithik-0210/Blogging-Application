package com.blogging.app.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LoginDto {

	
	@Email(message = "Please enter a valid email id..")
	@NotNull
	private String email;
	
	@NotNull(message = "password can not be null.")
	@Size(min = 4, max = 10, message = "password must be between 4 to 10 characters." )
	private String password;
}
