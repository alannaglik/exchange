package org.anaglik.exchange.handlery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.anaglik.exchange.enumy.DefinicjeBledu;
import org.anaglik.exchange.wyjatki.OdczytKontaUzytkownikaException;
import org.anaglik.exchange.wyjatki.OdczytKursuWalutyException;
import org.anaglik.exchange.wyjatki.PrzeliczenieWalutyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
	protected ResponseEntity<WyjatekDto> przeliczenieWalutyExceptionException(PrzeliczenieWalutyException ex) {
		log.error("Blad podczas przeliczenia waluty", ex);
		return budujResponse(ex, DefinicjeBledu.BLAD_PRZELICZENIA_WALUTY);
	}

	private ResponseEntity<WyjatekDto> budujResponse(Exception ex, DefinicjeBledu definicjeBledu) {
		var wyjatekDto = new WyjatekDto(ex.getMessage(), definicjeBledu.getOpisBledu());
		return new ResponseEntity<>(wyjatekDto, definicjeBledu.getStatusHttp());
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
