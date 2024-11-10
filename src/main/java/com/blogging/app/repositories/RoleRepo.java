package com.blogging.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogging.app.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {

	Role findByName(String name);
	
}
