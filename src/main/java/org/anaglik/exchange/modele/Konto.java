package org.anaglik.exchange.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
public class Konto {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long identyfikatorKonta;

	@Column(name = "imie", nullable = false, length = 50)
	private String imie;
	@Column(name = "nazwisko", nullable = false, length = 50)
	private String nazwisko;
	@OneToMany(mappedBy = "konto")
	private List<Saldo> salda;

	public Konto() {
		//dla JPA
	}
}
