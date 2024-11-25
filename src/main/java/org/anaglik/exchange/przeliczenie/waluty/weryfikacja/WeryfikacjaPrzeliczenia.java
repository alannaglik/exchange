package org.anaglik.exchange.przeliczenie.waluty.weryfikacja;

import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.modele.Saldo;
import org.anaglik.exchange.utils.BigDecimalUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Klasa dla weryfikacji sald konta użytkownika.
 */

public class WeryfikacjaPrzeliczenia {

	private boolean wynikWeryfikacji = false;
	private String komunikatBledu = null;

	public WeryfikacjaPrzeliczenia weryfikujIstnienieSaldaDlaWaluty(List<Saldo> saldaUzytkownika, Waluta waluta) {
		if (czyWeryfikacjaZBledem()) {
			return this;
		}

		if (CollectionUtils.isEmpty(saldaUzytkownika) || saldaUzytkownika.stream().noneMatch(s -> s.getWaluta().equals(waluta))) {
			wynikWeryfikacji = true;
			komunikatBledu = "Blad weryfikacji przeliczenia. Uzytkownik nie posiada konta z obsluga waluty: " + waluta.getKraj();
		}
		return this;
	}

	public WeryfikacjaPrzeliczenia weryfikujStanKonta(List<Saldo> saldaUzytkownika, Waluta waluta, BigDecimal kwotaWymiany) {
		if (czyWeryfikacjaZBledem()) {
			return this;
		}

		var saldo = saldaUzytkownika.stream().filter(s -> s.getWaluta().equals(waluta)).findFirst().get();
		if (BigDecimalUtils.czy(kwotaWymiany).jestWiekszeOd(saldo.getSaldoKonta())) {
			wynikWeryfikacji = true;
			komunikatBledu = "Blad weryfikacji przeliczenia. Uzytkownik nie posiada odpowiednich srodkow dla wymiany";
		}

		return this;
	}

	public boolean czyWeryfikacjaZBledem() {
		return wynikWeryfikacji;
	}


	public String komunikatBledu() {
		return komunikatBledu;
	}

}
