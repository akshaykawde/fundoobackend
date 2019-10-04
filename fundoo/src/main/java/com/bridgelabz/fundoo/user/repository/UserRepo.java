package com.bridgelabz.fundoo.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.notes.model.Note;
import com.bridgelabz.fundoo.user.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	public Optional<User> findByEmailId(String EmailId);
	public Optional<User> findByuserId(Long Id);

//	public Optional<User>findById(long userId);
	//public void save(Object object);
	
	
}
