package online.renanlf.sysproc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import online.renanlf.sysproc.exceptions.EmailNotFoundException;
import online.renanlf.sysproc.model.User;
import online.renanlf.sysproc.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public User findByEmail(String email) {
		return repo.findByEmail(email)
				.orElseThrow(() -> new EmailNotFoundException(email));
	}

	public User findByEmailAndPassword(String email, String password) {
		return repo.findByEmailAndPassword(email, password)
				.orElseThrow(() -> new EmailNotFoundException(email));
	}

	public User save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return repo.save(user);
	}

}
