package online.renanlf.sysproc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import online.renanlf.sysproc.model.Prosecution;
import online.renanlf.sysproc.repository.ProsecutionRepository;

@Service
public class ProsecutionService {

	@Autowired
	private ProsecutionRepository repo;
	
	@Autowired
	private UserService userService;
	
	public Optional<Prosecution> findByProtocol(String protocol) {
		return repo.findByProtocol(protocol);
	}
	
	public List<Prosecution> findByDescriptionLike(String description) {
		return repo.findByDescriptionLike(description);
	}
	
	public List<Prosecution> findByUserEmail(String email) {
		var user = userService.findByEmail(email);
		
		return repo.findByUser(user);
	}

	public Prosecution save(Prosecution prosecution, String email) {
		prosecution.setUser(userService.findByEmail(email));
		
		return repo.save(prosecution);
	}
}
