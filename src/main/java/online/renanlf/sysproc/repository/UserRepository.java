package online.renanlf.sysproc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import online.renanlf.sysproc.model.User;

public interface UserRepository  extends JpaRepository<User, Long> {
	
	@Query(value = "SELECT u FROM User u WHERE u.token = :token")
	Optional<User> findByToken(@Param("token") String token);

	@Query(value = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
	Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
	
	@Query(value = "SELECT u FROM User u WHERE u.email = :email")
	Optional<User> findByEmail(@Param("email") String email);
	
}
