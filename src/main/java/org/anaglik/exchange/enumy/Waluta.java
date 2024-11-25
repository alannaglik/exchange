package org.anaglik.exchange.enumy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeracja dla obsługiwanych walut.
 */

@Getter
@RequiredArgsConstructor
public enum Waluta {

	ZLOTY("PLN", "Polska"),
	DOLAR_AMERYKANSKI("USD", "Stany Zjednoczonone"),
	EURO("EUR", "Europa");

	private final String kodWaluty;
	private final String kraj;

	/**
	 * Metoda wyszukuje enum poprzez przekazanie wartości 'kodWaluty'
	 *
	 * @param kod - wartość, po której wyszukiwany jest właściwy enum
	 * @return - zwracany enum
	 */
	public static Waluta getByKodWaluty(String kod) {
		for (Waluta r : values()) {
			if (r.kodWaluty.equals(kod)) {
				return r;
			}
		}
		return null;
	}
}
