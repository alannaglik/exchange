package org.anaglik.exchange.kontrolery;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.kontrolery.payload.SaldoPayloadRecord;
import org.anaglik.exchange.serwisy.KontoService;
import org.anaglik.exchange.serwisy.SaldoService;
import org.anaglik.exchange.utils.WynikOdpowiedziUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
public class SaldoController {

	private final SaldoService saldoService;
	private final KontoService kontoService;

	@PutMapping("/{identyfikatorKonta}/przeliczDoPln/{kodWaluty}/{kwota}")
	@Validated
	public ResponseEntity aktualizujSaldoDoPln(@Valid SaldoPayloadRecord saldo, BindingResult bindingResult) {
		log.info("Wykonuje metode aktualizujSaldoDoPln.");

		if (bindingResult.hasErrors()) {
			log.error("Blad walidacji parametru przy tworzeniu salda.");
			return WynikOdpowiedziUtils.bladParametruWywolania(bindingResult);
		}

		return ResponseEntity.ok(saldoService.aktualizujKontoDlaPrzeliczeniaWaluty(saldo.identyfikatorKonta(),
				Waluta.getByKodWaluty(saldo.kodWaluty()),
				Waluta.ZLOTY,
				BigDecimal.valueOf(saldo.kwota())));
	}

	@PutMapping("/{identyfikatorKonta}/przeliczZPln/{kodWaluty}/{kwota}")
	@Validated
	public ResponseEntity aktualizujSaldoZPln(@Valid SaldoPayloadRecord saldo, BindingResult bindingResult) {
		log.info("Wykonuje metode przeliczZPln.");

		if (bindingResult.hasErrors()) {
			log.error("Blad walidacji parametru przy tworzeniu salda.");
			return WynikOdpowiedziUtils.bladParametruWywolania(bindingResult);
		}

		return ResponseEntity.ok(saldoService.aktualizujKontoDlaPrzeliczeniaWaluty(saldo.identyfikatorKonta(),
				Waluta.ZLOTY,
				Waluta.getByKodWaluty(saldo.kodWaluty()),
				BigDecimal.valueOf(saldo.kwota())));
	}
}
