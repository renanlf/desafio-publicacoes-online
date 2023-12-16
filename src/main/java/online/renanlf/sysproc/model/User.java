package online.renanlf.sysproc.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "login")
@EqualsAndHashCode(callSuper = false)
public @Data class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3698753817906834239L;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "email", unique = true)
	@NotBlank(message = "O e-mail deve ser fornecido")
	private String email;

	@NotBlank(message = "O nome deve ser fornecido")
	private String name;

	@JsonProperty(access = Access.WRITE_ONLY)
	@NotBlank(message = "A senha deve ser fornecida")
	private String password;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(unique = true)
	private String token;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	@Override
	public boolean isEnabled() {
		return true;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	@Override
	public String getUsername() {
		return email;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
