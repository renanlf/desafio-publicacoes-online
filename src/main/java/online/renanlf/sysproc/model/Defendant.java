package online.renanlf.sysproc.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "defendant")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE defendant SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public @Data class Defendant {
	
	@Id @GeneratedValue
	private long id;
	
	@NotBlank(message = "Nome do réu é obrigatório")
	private String name;
	
	@Column(unique = true)
	@NotBlank(message = "CPF/CNPJ do réu é campo obrigatório")
	private String cpfCnpj;
	
	private String address;
	
	private String phone;
	
	private String record;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean deleted = false;
	
}
