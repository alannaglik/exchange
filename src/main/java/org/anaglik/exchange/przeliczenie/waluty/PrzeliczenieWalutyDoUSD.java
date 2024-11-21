package org.anaglik.exchange.przeliczenie.waluty;

import lombok.RequiredArgsConstructor;
import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.modele.Konto;
import org.anaglik.exchange.serwisy.OdczytKursuWalutyService;
import org.anaglik.exchange.utils.BigDecimalUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Klasa obsługuje metody dla przeliczenia waluty do USD.
 */

@Component
@RequiredArgsConstructor
public class PrzeliczenieWalutyDoUSD implements IPrzeliczenieWaluty {

	private static final int PRECYZJA = 4;

	private final OdczytKursuWalutyService odczytKursuWalutyService;

	/**
	 * Aby była możliwa wymiana waluty do PLN użytkownik powinien posiadać środki w PLN
	 */
	@Override
	public boolean weryfikujMozliwoscPrzeliczeniaWaluty(Konto konto) {
		return BigDecimalUtils.czy(konto.getSaldoKontaPLN()).jestWiekszeOd(BigDecimal.ZERO);
	}

	/**
	* Założenie:
	* Pobieramy aktualny kurs przeliczenia, aktualizujemy przeliczaną wartość, zerujemy saldo, z którego przeliczaliśmy.
	 */
	@Override
	public Konto wykonajPrzeliczenieWaluty(Konto konto, Waluta przeliczonaWaluta) {
		BigDecimal pobranyKursWaluty = odczytKursuWalutyService.odczytajKursWaluty(przeliczonaWaluta);
		// metoda weryfikujMozliwoscPrzeliczeniaWaluty zabezpiecza przed dzieleniem przez zero
		konto.setSaldoKontaUSD(konto.getSaldoKontaPLN().divide(pobranyKursWaluty, PRECYZJA, RoundingMode.HALF_UP));
		konto.setSaldoKontaPLN(BigDecimal.ZERO);
		return konto;
	}
}
