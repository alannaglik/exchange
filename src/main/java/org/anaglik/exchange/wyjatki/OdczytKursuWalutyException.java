package org.anaglik.exchange.wyjatki;

/**
 * Wyjątek dla odczytu waluty.
 */
public class OdczytKursuWalutyException extends RuntimeException {

	public OdczytKursuWalutyException(String message) {
		super(message);
	}

	public OdczytKursuWalutyException(String message, Throwable cause) {
		super(message, cause);
	}
}
