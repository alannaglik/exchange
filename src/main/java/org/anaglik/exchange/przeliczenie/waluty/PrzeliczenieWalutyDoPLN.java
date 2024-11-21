package org.anaglik.exchange.przeliczenie.waluty;

import lombok.RequiredArgsConstructor;
import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.modele.Konto;
import org.anaglik.exchange.serwisy.OdczytKursuWalutyService;
import org.anaglik.exchange.utils.BigDecimalUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Klasa obsługuje metody dla przeliczenia waluty do PLN.
 */

@Component
@RequiredArgsConstructor
public class PrzeliczenieWalutyDoPLN implements IPrzeliczenieWaluty {

	private final OdczytKursuWalutyService odczytKursuWalutyService;

	/**
	 * Aby była możliwa wymiana waluty do PLN użytkownik powinien posiadać środki w USD
	 */
	@Override
	public boolean weryfikujMozliwoscPrzeliczeniaWaluty(Konto konto) {
		return BigDecimalUtils.czy(konto.getSaldoKontaUSD()).jestWiekszeOd(BigDecimal.ZERO);
	}

	/**
	 * Założenie:
	 * Pobieramy aktualny kurs przeliczenia, aktualizujemy przeliczoną wartość, zerujemy saldo, z którego przeliczaliśmy.
	 */
	@Override
	public Konto wykonajPrzeliczenieWaluty(Konto konto, Waluta przeliczonaWaluta) {
		BigDecimal pobranyKursWaluty = odczytKursuWalutyService.odczytajKursWaluty(przeliczonaWaluta);
		konto.setSaldoKontaPLN(pobranyKursWaluty.multiply(konto.getSaldoKontaUSD()));
		konto.setSaldoKontaUSD(BigDecimal.ZERO);
		return konto;
	}
}
