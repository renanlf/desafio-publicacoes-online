package online.renanlf.sysproc.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
public @Data class LoginRequisition {
	
	@Email(message = "It must be a valid email")
	private String email;
	
	@NotBlank(message = "The password must be non empty")
	private String password;

}
