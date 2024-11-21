package org.anaglik.exchange.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Encja reprezentująca konto użytkownika
 */
@Data
@Builder
@Entity
@Table(name = "konto")
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
	@Column(name = "saldo_konta_pln", nullable = false, precision = 12, scale = 4)
	private BigDecimal saldoKontaPLN;
	@Column(name = "saldo_konta_usd", precision = 12, scale = 4)
	private BigDecimal saldoKontaUSD;

	public Konto() {
		//dla JPA
	}
}
