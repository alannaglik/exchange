package org.anaglik.exchange.kontrolery;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.serwisy.SaldoService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Kontroler udostępniający usługi dla salda konta użytkownika.
 */

@RestController
@RequestMapping("/api/salda")
@AllArgsConstructor
@Slf4j
@Validated
public class SaldoController {

	private final SaldoService saldoService;

	@PutMapping("/{identyfikatorKonta}/przeliczDoPln/{kodWaluty}/{kwota}")
	public ResponseEntity aktualizujSaldoDoPln(
			@PathVariable @NotNull (message = "Pole identyfikator konta jest wymagany") long identyfikatorKonta,
			@PathVariable @NotEmpty(message = "Pole kod waluty jest wymagany") @Size(min = 3, max = 3, message = "Pole kod waluty musi zawierać 3 znaki") String kodWaluty,
			@PathVariable @NotNull(message = "Pole kwota jest wymagana") double kwota) {

		log.info("Wykonuje metode aktualizujSaldoDoPln.");
		return ResponseEntity.ok(saldoService.aktualizujKontoDlaPrzeliczeniaWaluty(identyfikatorKonta,
				Waluta.getByKodWaluty(kodWaluty),
				Waluta.ZLOTY,
				BigDecimal.valueOf(kwota)));
	}

	@PutMapping("/{identyfikatorKonta}/przeliczZPln/{kodWaluty}/{kwota}")
	public ResponseEntity aktualizujSaldoZPln(
			@PathVariable @NotNull(message = "Pole identyfikator konta jest wymagany") long identyfikatorKonta,
			@PathVariable @NotEmpty(message = "Pole kod waluty jest wymagany") @Size(min = 3, max = 3, message = "Pole kod waluty musi zawierać 3 znaki") String kodWaluty,
			@PathVariable @NotNull(message = "Pole kwota jest wymagana") double kwota) {
		log.info("Wykonuje metode przeliczZPln.");

		return ResponseEntity.ok(saldoService.aktualizujKontoDlaPrzeliczeniaWaluty(identyfikatorKonta,
				Waluta.ZLOTY,
				Waluta.getByKodWaluty(kodWaluty),
				BigDecimal.valueOf(kwota)));
	}
}
