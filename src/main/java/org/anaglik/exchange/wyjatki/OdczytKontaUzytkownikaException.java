package org.anaglik.exchange.wyjatki;

/**
 * Wyjątek dla odczytu konta użytkownika.
 */

public class OdczytKontaUzytkownikaException extends RuntimeException {

	public OdczytKontaUzytkownikaException(String message) {
		super(message);
	}
}
