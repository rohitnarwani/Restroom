package com.locatar.restroom.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.locatar.restroom.model.User;

public interface UserRepository extends CrudRepository<User, String> {
	@Override
	void delete(User user);

	List<User> findByUserIdAndPassword(long userId, String password);

}
