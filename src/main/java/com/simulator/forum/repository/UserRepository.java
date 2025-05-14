package com.simulator.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.simulator.forum.entity.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail , Long>
{
	UserDetail findByEmail(String username);
}
