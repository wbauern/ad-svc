package com.sovrn.ad.dal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sovrn.ad.dal.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	public User findById(int userid);
}
