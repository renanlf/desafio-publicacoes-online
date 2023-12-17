package online.renanlf.sysproc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import online.renanlf.sysproc.model.Prosecution;
import online.renanlf.sysproc.repository.ProsecutionRepository;

@Service
public class ProsecutionService {

	@Autowired
	private ProsecutionRepository repo;
	
	@Autowired
	private UserService userService;
	
	public List<Prosecution> findByUserEmail(String email) {
		return repo.findByUserEmail(email);
	}

	public Prosecution save(Prosecution prosecution, String email) {
		prosecution.setUser(userService.findByEmail(email));
		
		return repo.save(prosecution);
	}

	public Optional<Prosecution> findByUserEmailAndProtocol(String email, String protocol) {
		return repo.findByUserEmailAndProtocol(email, protocol);
	}

	public List<Prosecution> findByUserEmailAndDescriptionContainingIgnoreCase(String email, String description) {
		return repo.findByUserEmailAndDescriptionContainingIgnoreCase(email, description);
	}

	@Transactional
	public void deleteByUserEmailAndProtocol(String email, String protocol) {
		repo.deleteByUserEmailAndProtocol(email, protocol);
	}
}
