package online.renanlf.sysproc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import online.renanlf.sysproc.model.User;

public interface UserRepository  extends JpaRepository<User, Long> {
	
	Optional<User> findByToken(String token);

	Optional<User> findByEmailAndPassword(String email, String password);
	
	Optional<User> findByEmail(String email);
	
}
