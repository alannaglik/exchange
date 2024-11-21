package org.anaglik.exchange.przeliczenie.waluty;

import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.modele.Konto;

/**
 * Interface dla klas realizujących przeliczenia waluty.
 */

public interface IPrzeliczenieWaluty {
	boolean weryfikujMozliwoscPrzeliczeniaWaluty(Konto konto);

	Konto wykonajPrzeliczenieWaluty(Konto konto, Waluta przeliczonaWaluta);

}
