package online.renanlf.sysproc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import online.renanlf.sysproc.model.Prosecution;
import online.renanlf.sysproc.model.User;

public interface ProsecutionRepository extends JpaRepository<Prosecution, Long> {
	
	Optional<Prosecution> findByProtocol(String protocol);
	
	List<Prosecution> findByDescriptionLike(String description);
	
	List<Prosecution> findByUser(User user);

}
