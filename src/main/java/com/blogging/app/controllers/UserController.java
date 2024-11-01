package com.blogging.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogging.app.payloads.ApiResponse;
import com.blogging.app.payloads.UserDto;
import com.blogging.app.payloads.UserResponseDto;
import com.blogging.app.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;

	
	
	@GetMapping("/csrf-token")
	public CsrfToken getCsrfToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<UserResponseDto> getAllUser(@RequestParam(name = "pageNo")int pageNo, @RequestParam(name = "pageSize") int pageSize, @RequestParam(name="sortBy") String sortBy, @RequestParam(name="sortDir") String sortDir ){
		UserResponseDto users =  this.userService.getAllUsers(pageNo, pageSize,sortBy,sortDir);
		return new ResponseEntity<UserResponseDto>(users, HttpStatus.OK);
	}
	
	@GetMapping("/getUser/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable("id") int id){
		UserDto user = this.userService.getUserById(id);
		return new ResponseEntity<UserDto>(user, HttpStatus.OK);
	}
	
	@PutMapping("/updateUser/{id}")
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable("id") int userId){
		UserDto updatedUser = this.userService.updateUser(userDto, userId);
		return new ResponseEntity<UserDto>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") int userId){
		try {
			this.userService.deleteUser(userId);
			return new ResponseEntity<ApiResponse>(new ApiResponse("user deleted succesfully....", true),HttpStatus.OK);			
		}catch (Exception e) {
			System.out.println(e);
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");

		}
	}
	
}
