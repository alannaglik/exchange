package org.anaglik.exchange.kontrolery;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anaglik.exchange.kontrolery.payload.KontoPayloadRecord;
import org.anaglik.exchange.modele.Konto;
import org.anaglik.exchange.serwisy.KontoService;
import org.anaglik.exchange.serwisy.SaldoService;
import org.anaglik.exchange.utils.SaldoUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Kontroler udostępniający usługi dla konta użytkownika.
 */

@RestController
@RequestMapping("/api/konta")
@AllArgsConstructor
@Slf4j
public class KontoController {

	private final KontoService kontoService;
	private final SaldoService saldoService;

	@PostMapping
	public ResponseEntity<Long> utworzKontoUzytkownika(@Valid @RequestBody KontoPayloadRecord kontoPayload, BindingResult bindingResult) {
		log.info("Wykonuje metode utworzKontoUzytkownika.");
		if (bindingResult.hasErrors()) {
			log.error("Blad walidacji przy tworzeniu konta uzytkownika.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		var konto = Konto.builder()
				.imie(kontoPayload.imie())
				.nazwisko(kontoPayload.nazwisko()).build();

		final Long idUtworzonegoKonta = kontoService.utworzKonto(konto);
		saldoService.utworzSalda(List.of(SaldoUtils.utworzSaldoPln(kontoPayload.saldoKontaPLN(), konto),
										 SaldoUtils.utworzSaldoUsd(kontoPayload.saldoKontaUSD(), konto)));
		return ResponseEntity.ok(idUtworzonegoKonta);
	}

	@GetMapping("/{identyfikatorKonta}")
	public ResponseEntity<Konto> pobierzKontoUzytkownika(@PathVariable long identyfikatorKonta) {
		log.info("Wykonuje metode pobierzKontoUzytkownika dla id = {}.", identyfikatorKonta);
		final Optional<Konto> pobraneKontoUzytkownika = kontoService.pobierzKontoUzytkownika(identyfikatorKonta);
		if (pobraneKontoUzytkownika.isPresent()) {
			return ResponseEntity.ok(pobraneKontoUzytkownika.get());
		}

		log.error("Pobranie danych konta uzytkownika dla id = {}, nie jest mozliwe.", identyfikatorKonta);
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{identyfikatorKonta}")
	public ResponseEntity usunKontoUzytkownika(@PathVariable long identyfikatorKonta) {
		log.info("Wykonuje metode usunKontoUzytkownika dla id = {}.", identyfikatorKonta);
		final Optional<Konto> pobraneKontoUzytkownika = kontoService.pobierzKontoUzytkownika(identyfikatorKonta);
		if (pobraneKontoUzytkownika.isPresent()) {
			kontoService.usunKontoUzytkownika(pobraneKontoUzytkownika.get());
			return new ResponseEntity(HttpStatus.OK);
		}

		log.error("Usuniecie konta uzytkownika dla id = {}, nie jest mozliwe.", identyfikatorKonta);
		return ResponseEntity.notFound().build();
	}

	@GetMapping
	public ResponseEntity<Long> zwrocLiczbeKontUzytkownikow() {
		log.info("Wykonuje metode zwrocLiczbeKontUzytkownikow.");
		return ResponseEntity.ok(kontoService.zwrocLiczbeKontUzytkownikow());
	}

}
