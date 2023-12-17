package online.renanlf.sysproc.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import online.renanlf.sysproc.model.Prosecution;
import online.renanlf.sysproc.service.ProsecutionService;

@RestController
@RequestMapping("/prosecutions")
public class ProsecutionController {
	
	@Autowired
	private ProsecutionService service;

	@GetMapping
	public List<Prosecution> getAllOrByDescriptionContainingIgnoreCase(@RequestParam(name = "description", required = false) Optional<String> description) {
		var email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	
		return description
				.map(d -> service.findByUserEmailAndDescriptionContainingIgnoreCase(email, d))
				.orElse(service.findByUserEmail(email));
	}
	
	@PostMapping
	public Prosecution post(@Valid @RequestBody Prosecution prosecution) {
		var email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		return service.save(prosecution, email);
	}
	
	@GetMapping("{protocol}")
	public ResponseEntity<Prosecution> getByProtocol(@PathVariable(name = "protocol") String protocol) {
		var email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		var prosecution = service.findByUserEmailAndProtocol(email, protocol);
		
		// if there is no prosecution found, then it sends a forbidden status, for security reasons
		return prosecution
				.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
	}
	
	@DeleteMapping("{protocol}")
	public void deleteByProtocol(@PathVariable(name = "protocol") String protocol) {
		var email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		service.deleteByUserEmailAndProtocol(email, protocol);
	}
	
}
