package org.anaglik.exchange.serwisy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.modele.Konto;
import org.anaglik.exchange.przeliczenie.waluty.IPrzeliczenieWaluty;
import org.anaglik.exchange.przeliczenie.waluty.PrzeliczenieWalutyDoPLN;
import org.anaglik.exchange.przeliczenie.waluty.PrzeliczenieWalutyDoUSD;
import org.anaglik.exchange.repozytoria.KontoRepository;
import org.anaglik.exchange.wyjatki.WeryfikacjaPrzeliczeniaWalutyException;
import org.springframework.stereotype.Service;

/**
 * Klasa realizuje przeliczenie waluty dla konta użytkownika.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class PrzeliczenieWalutyService {

	private final KontoRepository kontoRepository;
	private final PrzeliczenieWalutyDoPLN przeliczenieWalutyDoPLN;
	private final PrzeliczenieWalutyDoUSD przeliczenieWalutyDoUSD;

	/**
	 * Metoda realizuje przeliczenie dla podanej waluty
	 *
	 * @param konto             konto użytkownika
	 * @param przeliczonaWaluta przeliczona waluta
	 * @return przeliczone konto
	 */
	public Konto przeliczWalute(Konto konto, Waluta przeliczonaWaluta) {

		IPrzeliczenieWaluty przeliczenieWaluty = zwrocTypPrzeliczeniaWaluty(przeliczonaWaluta);

		boolean wynikWeryfikacji = przeliczenieWaluty.weryfikujMozliwoscPrzeliczeniaWaluty(konto);
		if (!wynikWeryfikacji) {
			throw new WeryfikacjaPrzeliczeniaWalutyException("Blad weryfikacji dla przeliczenia");
		}
		var zaktualizowaneKonto = przeliczenieWaluty.wykonajPrzeliczenieWaluty(konto, przeliczonaWaluta);
		kontoRepository.save(zaktualizowaneKonto);
		return zaktualizowaneKonto;
	}

	private IPrzeliczenieWaluty zwrocTypPrzeliczeniaWaluty(Waluta przeliczonaWaluta) {
		log.info("Wykonuje metode zwrocTypPrzeliczeniaWaluty dla waluty: {}", przeliczonaWaluta);
		IPrzeliczenieWaluty przeliczenieWaluty;

		switch (przeliczonaWaluta) {
			case ZLOTY -> przeliczenieWaluty = przeliczenieWalutyDoPLN;
			case DOLAR_AMERYKANSKI -> przeliczenieWaluty = przeliczenieWalutyDoUSD;
			default -> {
				log.error("Blad zwracania typu przeliczenia waluty. Wprowadzono nieobslugiwana walute: {}", przeliczonaWaluta);
				throw new IllegalStateException("Blad zwracania typu przeliczenia waluty. Wprowadzono nieobslugiwana walute: {}" + przeliczonaWaluta);
			}
		}
		return przeliczenieWaluty;
	}

}
