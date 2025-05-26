package com.simulator.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulator.forum.entity.Remember;

public interface RememberMeRepository extends JpaRepository<Remember , String>{

}
