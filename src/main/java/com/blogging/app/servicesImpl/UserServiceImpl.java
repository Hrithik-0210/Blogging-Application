package com.blogging.app.servicesImpl;

import java.util.ArrayList;
import java.util.Collection;
//import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogging.app.entities.Role;
import com.blogging.app.entities.User;
import com.blogging.app.payloads.LoginDto;
import com.blogging.app.payloads.UserDto;
import com.blogging.app.payloads.UserResponseDto;
import com.blogging.app.repositories.RoleRepo;
import com.blogging.app.repositories.UserRepo;
import com.blogging.app.services.UserService;
import com.blogging.app.utils.JwtUtils;
import com.blogging.app.exception.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	@Lazy
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	
	@Override
	public UserDto createUser(UserDto userDto) {
		User user = dtoToEntity(userDto);
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword); // Set the encoded password
		 Set<Role> roles = new HashSet<>();
		 if(userDto.getUserRole() != null && !userDto.getUserRole().isEmpty()) {
			 for(String roleName : userDto.getUserRole()) {
				 Role role = roleRepo.findByName(roleName);
				 if(role!=null) {
					 roles.add(role);
				 }else {
					 new RuntimeException("Role not found "+ roleName);
				 }
			 }
		 }
		 user.setRoles(roles);
		User savedUser = this.userRepo.save(user);
		return this.entityToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		user.setPassword(userDto.getPassword());
		User updatedUser = this.userRepo.save(user);
		return this.entityToDto(updatedUser);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		return this.entityToDto(user);
	}

	@Override
	public UserResponseDto getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<User> users = this.userRepo.findAll(pageable);
		List<UserDto> userDto = users.stream().map(user -> this.entityToDto(user)).collect(Collectors.toList());
		UserResponseDto userResDto = new UserResponseDto();
		userResDto.setUsers(userDto);
		userResDto.setPageNo(users.getNumber());
		userResDto.setPageSize(users.getSize());
		userResDto.setTotalElement(users.getTotalElements());
		userResDto.setTotalPages(users.getTotalPages());
		userResDto.setLastPage(users.isLast());
		return userResDto;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		this.userRepo.delete(user);

	}

	public User dtoToEntity(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setAbout(userDto.getAbout());
//		user.setPassword(userDto.getPassword());
		return user;
	}

	public UserDto entityToDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
//		userDto.setId(user.getId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setAbout(user.getAbout());
//		userDto.setPassword(user.getPassword());
		 Set<String> roleNames = user.getRoles().stream()
			        .map(Role::getName) // Assuming your Role entity has a getName() method
			        .collect(Collectors.toSet());
			    userDto.setUserRole(roleNames); 
		return userDto;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username);
		if (user == null) {
			System.out.println("User not found");
			throw new UsernameNotFoundException("user not found");
		}
		// Map roles from the database (with ROLE_ prefix) to GrantedAuthority
	    Collection<GrantedAuthority> authorities = user.getRoles().stream()
	        .map(role -> new SimpleGrantedAuthority(role.getName())) // Role with ROLE_ prefix
	        .collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
	}

	@Override
	public ResponseEntity<Map<String, String>> verifyUser(LoginDto loginDto) {
	    try {
	        // Fetch the user from the database
	        User user = userRepo.findByEmail(loginDto.getEmail());
	        if (user != null && user.getIsLoggedIn() == 1) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User is already logged in."));
	        }
	        // Authenticate the user based on email and password
	        Authentication authentication = authenticationManager
	                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

	        if (authentication.isAuthenticated()) {
	        	 // Fetch user roles as a Set<String>
	            Set<String> roles = user.getRoles().stream()
	                    .map(role -> role.getName()) // Extract the role name (e.g., "ADMIN" or "USER")
	                    .collect(Collectors.toSet());
	            
	            String accessToken = jwtUtils.generateToken(loginDto.getEmail(), roles);
	            String refreshToken = jwtUtils.generateRefreshToken(loginDto.getEmail(), roles);

	            // Mark the user as logged in
	            user.setIsLoggedIn(1);
	            userRepo.save(user);
	            // Prepare the response containing the tokens
	            Map<String, String> response = new HashMap<>();
	            response.put("access_token", accessToken);
	            response.put("refresh_token", refreshToken);
	            response.put("message", "Login Successfully");
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("message", "Username or password is wrong."));
	    }
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Username or password is wrong."));
	}

}
