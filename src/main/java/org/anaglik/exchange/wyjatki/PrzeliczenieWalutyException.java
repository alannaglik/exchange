package org.anaglik.exchange.wyjatki;

/**
 * Wyjątek dla przeliczenia waluty.
 */
public class PrzeliczenieWalutyException extends RuntimeException {

	public PrzeliczenieWalutyException(String message) {
		super(message);
	}
}
