package online.renanlf.sysproc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public List<Prosecution> get() {
		var email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		return service.findByUserEmail(email);
	}
	
	@PostMapping
	public Prosecution post(@Valid @RequestBody Prosecution prosecution) {
		var email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		return service.save(prosecution, email);
	}
	
}
