package org.anaglik.exchange.wyjatki;

/**
 * Wyjątek dla przeliczenia waluty.
 *
 * @author Pentacomp Systemy Informatyczne S.A.
 */
public class WeryfikacjaPrzeliczeniaWalutyException extends RuntimeException {

	public WeryfikacjaPrzeliczeniaWalutyException(String message) {
		super(message);
	}
}
