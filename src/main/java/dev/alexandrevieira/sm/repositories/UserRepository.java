package dev.alexandrevieira.sm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.alexandrevieira.sm.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
}
