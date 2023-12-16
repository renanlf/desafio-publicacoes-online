package online.renanlf.sysproc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import online.renanlf.sysproc.model.LoginRequisition;
import online.renanlf.sysproc.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@PostMapping
	public String performLogin(@Valid @RequestBody LoginRequisition loginReq) {		
		return loginService.authenticate(loginReq);
	}
}
