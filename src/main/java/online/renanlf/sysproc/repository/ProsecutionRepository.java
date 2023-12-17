package online.renanlf.sysproc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import online.renanlf.sysproc.model.Prosecution;

public interface ProsecutionRepository extends JpaRepository<Prosecution, Long> {
	
	List<Prosecution> findByUserEmail(String email);
	
	Optional<Prosecution> findByProtocol(String protocol);

	Optional<Prosecution> findByUserEmailAndProtocol(String email, String protocol);

	List<Prosecution> findByUserEmailAndDescriptionContainingIgnoreCase(String email, String description);

	long deleteByUserEmailAndProtocol(String email, String protocol);

}
