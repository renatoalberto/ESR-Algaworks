package com.algaworks.algafood.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.algaworks.algafood.core.validation.Groups;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@Getter                                             // lombok
//@Setter                                             // lombok
@Data                                                 // lombok
@EqualsAndHashCode(onlyExplicitlyIncluded = true)     // lombok
@JsonRootName("gastronomia")                          // anotação jackson altera o nome da representação percebida no XML
@Entity
@Table(name = "tab_cozinhas")
public class Cozinha {
	
	@NotNull(groups = Groups.CozinhaId.class)
	@EqualsAndHashCode.Include                        // lombok
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(name = "nom_cozinha", length = 30, nullable = false)
	private String nome;
	
	@OneToMany(mappedBy = "cozinha")
	private List<Restaurante> restaurantes = new ArrayList<Restaurante>();

	@Override
	public String toString() {
		return "Cozinha [id=" + id + ", nome=" + nome + "]";
	}


}
