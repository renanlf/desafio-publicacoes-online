package online.renanlf.sysproc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import online.renanlf.sysproc.model.Defendant;
import online.renanlf.sysproc.model.Prosecution;
import online.renanlf.sysproc.service.DefendantService;
import online.renanlf.sysproc.service.ProsecutionService;

@RestController
@RequestMapping("/prosecutions/{protocol}/defendants")
public class ProsecutionDefendantController {
	
	@Autowired
	private ProsecutionService prosecutionService;	
	@Autowired
	private DefendantService defendantService;

	@GetMapping
	public ResponseEntity<List<Defendant>> getDefendantsByProtocol(@PathVariable(name = "protocol") String protocol) {
		var email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		return prosecutionService.findByUserEmailAndProtocol(email, protocol)
				.map(p -> new ResponseEntity<>(p.getDefendants(), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.FORBIDDEN));
	}
	
	/**
	 * this method returns a FORBIDDEN status when no prosecution is found.
	 * this method also returns a NOT FOUND status when no defendant is found.
	 * otherwise, it returns an OK status and a Prosecution object.
	 * @param protocol
	 * @param cpfCnpj
	 * @return
	 */
	@PutMapping("{cpfCnpj}")
	public ResponseEntity<Prosecution> addDefendantToProsecution(
			@PathVariable(name = "protocol") String protocol,
			@PathVariable(name = "cpfCnpj") String cpfCnpj) {
		
		var email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		var prosecution = prosecutionService.findByUserEmailAndProtocol(email, protocol);	
		if(prosecution.isEmpty()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		
		var defendant = defendantService.findByCpfCnpj(cpfCnpj);
		if(defendant.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		prosecution.get().getDefendants().add(defendant.get());
		return new ResponseEntity<>(prosecutionService.update(prosecution.get()), HttpStatus.OK);
	}

}
