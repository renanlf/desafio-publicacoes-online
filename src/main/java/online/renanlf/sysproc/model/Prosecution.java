package online.renanlf.sysproc.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prosecution")
@NoArgsConstructor
public @Data class Prosecution {

	@Id @GeneratedValue
	private long id;
	
	@ManyToOne
	private User user;
	
	@Column(unique = true)
	@NotBlank(message = "Protocolo não pode ser vazio")
	private String protocol;
	
	@NotNull(message = "Data de abertura do processo não pode ser vazia")
	private Date openingDate;
	
	private String description;
	
	private String court;
	
	private String district;
	
}
