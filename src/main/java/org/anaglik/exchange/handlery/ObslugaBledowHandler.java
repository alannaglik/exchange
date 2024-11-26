package org.anaglik.exchange.handlery;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.anaglik.exchange.enumy.DefinicjeBledu;
import org.anaglik.exchange.wyjatki.OdczytKontaUzytkownikaException;
import org.anaglik.exchange.wyjatki.OdczytKursuWalutyException;
import org.anaglik.exchange.wyjatki.PrzeliczenieWalutyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

/**
 * Obsługa wyjątków z API dla aplikacji.
 */

@Slf4j
@ControllerAdvice
public class ObslugaBledowHandler {
	/**
	 * Obsługuje wyjątek w komunikacji z NBP.
	 *
	 * @param ex przejęty wyjątek związany z błędem w komunikacji z NBP
	 * @return odpowiedź zawierająca kod i opis wyjątku
	 */
	@ExceptionHandler(OdczytKursuWalutyException.class)
	protected ResponseEntity<WyjatekDto> obsluzOdczytKursuWalutyException(OdczytKursuWalutyException ex) {
		log.error("Blad odpowiedzi z NBP", ex);
		return budujResponse(ex, DefinicjeBledu.BLAD_ODCZYTU_KURSU_WALUT);
	}

	/**
	 * Obsługuje wyjątek dla braku danych przy odczycie użytkownika.
	 *
	 * @param ex przejęty wyjątek związany z błędem braku danych przy odczycie użytkownika
	 * @return odpowiedź zawierająca kod i opis wyjątku
	 */
	@ExceptionHandler(OdczytKontaUzytkownikaException.class)
	protected ResponseEntity<WyjatekDto> obsluzOdczytKontaUzytkownikaException(OdczytKontaUzytkownikaException ex) {
		log.error("Blad odczytu dla nieistniejacego uzytkownika", ex);
		return budujResponse(ex, DefinicjeBledu.BRAK_DANYCH_PRZY_ODCZYCIE);
	}

	/**
	 * Obsługuje wyjątek dla błędu podczas przeliczania.
	 *
	 * @param ex przejęty wyjątek związany z błędem przeliczania
	 * @return odpowiedź zawierająca kod i opis wyjątku
	 */
	@ExceptionHandler(PrzeliczenieWalutyException.class)
	protected ResponseEntity<WyjatekDto> obsluzPrzeliczenieWalutyExceptionException(PrzeliczenieWalutyException ex) {
		log.error("Blad podczas przeliczenia waluty", ex);
		return budujResponse(ex, DefinicjeBledu.BLAD_PRZELICZENIA_WALUTY);
	}

	/**
	 * Obsługuje wyjątek dla błędu niepoprawnego parametru w adresie zapytania.
	 *
	 * @param ex przejęty wyjątek związany z błędem niepoprawnego parametru w adresie zapytania
	 * @return odpowiedź zawierająca kod i opis wyjątku
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<WyjatekDto> obsluzNieporawnyParametrWAdresieZapytaniaException(ConstraintViolationException ex) {
		log.error("Blad podczas walidacji zapytania", ex);
		var blad = ex.getConstraintViolations().stream().findFirst().orElse(null);
		var exception = new Exception("Blad walidacji zapytania: " + Objects.toString(blad.getMessage(), ""));
		return budujResponse(exception, DefinicjeBledu.NIEPORAWNY_PARAMETR_ZAPYTANIA);
	}

	private ResponseEntity<WyjatekDto> budujResponse(Exception ex, DefinicjeBledu definicjeBledu) {
		var wyjatekDto = new WyjatekDto(ex.getMessage(), definicjeBledu.getOpisBledu());
		return new ResponseEntity<>(wyjatekDto, definicjeBledu.getStatusHttp());
	}

	/**
	 * Obsługuje wyjątek dla błędu niepoprawnego parametru zapytania w body.
	 *
	 * @param ex przejęty wyjątek związany z błędem niepoprawnego parametru zapytania w body
	 * @return odpowiedź zawierająca kod i opis wyjątku
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<WyjatekDto> obsluzNieporawnyParametrWBodyException(MethodArgumentNotValidException ex) {
		log.error("Blad podczas walidacji zapytania", ex);
		var blad = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
		var exception = new Exception("Blad walidacji zapytania: " + Objects.toString(blad.getDefaultMessage(), ""));
		return budujResponse(exception, DefinicjeBledu.NIEPORAWNY_PARAMETR_ZAPYTANIA);
	}

	/**
	 * Wewnętrzna klasa pomocnicza dla zgłaszania wyjątków.
	 */

	@Getter
	@AllArgsConstructor
	static class WyjatekDto {
		private String opisBledu;
		private String definicjaBledu;
	}
}
