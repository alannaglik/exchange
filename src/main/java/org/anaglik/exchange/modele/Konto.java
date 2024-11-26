package org.anaglik.exchange.modele;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Encja reprezentująca konto użytkownika
 */

@Entity
@Table(name = "konto")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Konto implements Serializable {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "konto_sequence")
	@SequenceGenerator(
			name = "konto_sequence",
			sequenceName = "konto_sequence",
			allocationSize = 1,
			initialValue = 1)
	private Long identyfikatorKonta;

	@Column(name = "imie", nullable = false, length = 50)
	private String imie;
	@Column(name = "nazwisko", nullable = false, length = 50)
	private String nazwisko;
	@JsonManagedReference
	@OneToMany(mappedBy = "konto")
	private List<Saldo> salda;

	public Konto() {
		//dla JPA
	}
}
