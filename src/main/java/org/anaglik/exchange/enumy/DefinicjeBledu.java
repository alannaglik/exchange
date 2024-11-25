package org.anaglik.exchange.enumy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Definicje zwracanych błędów na potrzeby API aplikacji
 */
@Getter
@RequiredArgsConstructor
public enum DefinicjeBledu {

	BRAK_DANYCH_PRZY_ODCZYCIE("Brak danych przy odczycie", HttpStatus.INTERNAL_SERVER_ERROR),
	BLAD_ODCZYTU_KURSU_WALUT("Nieoczekiwany błąd przy odczycie z NBP", HttpStatus.INTERNAL_SERVER_ERROR),
	NIEPORAWNY_PARAMETR_ZAPYTANIA("Niepoprawny parametr zapytania", HttpStatus.BAD_REQUEST),
	BLAD_PRZELICZENIA_WALUTY("Nieporawne przeliczenie srodkow", HttpStatus.INTERNAL_SERVER_ERROR);

	private final String opisBledu;
	private final HttpStatus statusHttp;
}
