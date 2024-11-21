package com.blogging.app.services;


import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.blogging.app.payloads.LoginDto;
import com.blogging.app.payloads.UserDto;
import com.blogging.app.payloads.UserResponseDto;
 

public interface UserService {

	UserDto createUser(UserDto userDto);
	UserDto updateUser(UserDto userDto, Integer userId);
	UserDto getUserById(Integer userId);
	UserResponseDto getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);
	void deleteUser(Integer userId);
	ResponseEntity<Map<String , String>> verifyUser(LoginDto loginDto);
}
