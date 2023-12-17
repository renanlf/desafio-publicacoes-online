package online.renanlf.sysproc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import online.renanlf.sysproc.model.Defendant;
import online.renanlf.sysproc.service.DefendantService;

@RestController
@RequestMapping("/defendants")
public class DefendantController {
	
	@Autowired
	private DefendantService service;
	
	@PostMapping
	public Defendant postDefendant(@Valid @RequestBody Defendant defendant) {
		return service.save(defendant);
	}
	
	@GetMapping
	public List<Defendant> getAllDefendant() {
		return service.findAll();
	}
	
	@GetMapping("{cpfCnpj}")
	public ResponseEntity<Defendant> getDefendantByCpfCnpj(@PathVariable(name = "cpfCnpj") String cpfCnpj) {
		return service.findByCpfCnpj(cpfCnpj)
				.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
	}
	
	@DeleteMapping("{cpfCnpj}")
	public void deleteDefendant(@PathVariable(name = "cpfCnpj") String cpfCnpj) {
		service.deleteByCpfCnpj(cpfCnpj);
	}

}
