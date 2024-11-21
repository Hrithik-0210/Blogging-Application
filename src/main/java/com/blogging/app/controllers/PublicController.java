package com.blogging.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogging.app.payloads.LoginDto;
import com.blogging.app.payloads.UserDto;
import com.blogging.app.services.UserService;
import com.blogging.app.servicesImpl.UserServiceImpl;

import jakarta.validation.Valid;
//import jdk.internal.icu.impl.ICUBinary.Authenticate;

@RestController
@RequestMapping("/public")
public class PublicController {
	
//	@Autowired
//	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private UserService userService;
	
//	@Autowired
//	private AuthenticationManager authenticationManager;
	

	@PostMapping("/createUser")
	public ResponseEntity<UserDto> createUser(@Valid  @RequestBody UserDto userDto){
		UserDto createdUser= this.userService.createUser(userDto);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDto loginDto){
		return userService.verifyUser(loginDto);
	}
	
}
