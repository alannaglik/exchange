package org.anaglik.exchange.serwisy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.modele.Konto;
import org.anaglik.exchange.przeliczenie.waluty.IPrzeliczenieWaluty;
import org.anaglik.exchange.przeliczenie.waluty.PrzeliczenieWalutyDoPLN;
import org.anaglik.exchange.przeliczenie.waluty.PrzeliczenieWalutyDoUSD;
import org.anaglik.exchange.repozytoria.KontoRepository;
import org.anaglik.exchange.wyjatki.PrzeliczenieWalutyException;
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
	 * @param przeliczanaWaluta przeliczona waluta
	 * @return przeliczone konto
	 */
	public Konto przeliczWalute(Konto konto, Waluta przeliczanaWaluta) {

		IPrzeliczenieWaluty przeliczenieWaluty = zwrocTypPrzeliczeniaWaluty(przeliczanaWaluta);

		boolean wynikWeryfikacji = przeliczenieWaluty.weryfikujMozliwoscPrzeliczeniaWaluty(konto);
		if (!wynikWeryfikacji) {
			throw new PrzeliczenieWalutyException("Blad weryfikacji dla przeliczenia");
		}
		var zaktualizowaneKonto = przeliczenieWaluty.wykonajPrzeliczenieWaluty(konto, przeliczanaWaluta);
		kontoRepository.save(zaktualizowaneKonto);
		return zaktualizowaneKonto;
	}

	private IPrzeliczenieWaluty zwrocTypPrzeliczeniaWaluty(Waluta przeliczanaWaluta) {
		log.info("Wykonuje metode zwrocTypPrzeliczeniaWaluty dla waluty: {}", przeliczanaWaluta);
		IPrzeliczenieWaluty przeliczenieWaluty;

		switch (przeliczanaWaluta) {
			case ZLOTY -> przeliczenieWaluty = przeliczenieWalutyDoPLN;
			case DOLAR_AMERYKANSKI -> przeliczenieWaluty = przeliczenieWalutyDoUSD;
			default -> {
				log.error("Blad zwracania typu przeliczenia waluty. Wprowadzono nieobslugiwana walute: {}", przeliczanaWaluta);
				throw new PrzeliczenieWalutyException("Blad zwracania typu przeliczenia waluty. Wprowadzono nieobslugiwana walute: {}" + przeliczanaWaluta);
			}
		}
		return przeliczenieWaluty;
	}

}
