package online.renanlf.sysproc.model;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prosecution")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE prosecution SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public @Data class Prosecution {

	@Id @GeneratedValue
	private long id;
	
	@ManyToOne
	private User user;
	
	@ManyToMany
	private List<Defendant> defendants;
	
	@Column(unique = true)
	@NotBlank(message = "Protocolo não pode ser vazio")
	private String protocol;
	
	@NotNull(message = "Data de abertura do processo não pode ser vazia")
	private Date openingDate;
	
	private String description;
	
	private String court;
	
	private String district;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Boolean deleted = false;
	
}
