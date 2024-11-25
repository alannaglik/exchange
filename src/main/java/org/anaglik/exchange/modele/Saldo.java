package org.anaglik.exchange.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.anaglik.exchange.enumy.Waluta;

import java.math.BigDecimal;

/**
 * Encja reprezentująca saldo konta użytkownika
 */
@Entity
@Table(name = "saldo")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Saldo {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long identyfikatorSalda;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private Waluta waluta;

	@Column(name = "saldo_konta", nullable = false, precision = 12, scale = 4)
	private BigDecimal saldoKonta = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "konto_id")
	private Konto konto;

	public Saldo() {
		//dla JPA
	}
}
