package org.anaglik.exchange.serwisy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anaglik.exchange.enumy.KierunekPrzeliczania;
import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.modele.Konto;
import org.anaglik.exchange.modele.Saldo;
import org.anaglik.exchange.przeliczenie.waluty.weryfikacja.WeryfikacjaPrzeliczenia;
import org.anaglik.exchange.repozytoria.SaldoRepository;
import org.anaglik.exchange.wyjatki.PrzeliczenieWalutyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Klasa realizuje przeliczenie waluty dla konta użytkownika.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class PrzeliczenieWalutyService {

	private final OdczytKursuWalutyService odczytKursuWalutyService;
	private final SaldoRepository saldoRepository;


	/**
	 * Metoda realizuje przeliczenie dla podanej waluty
	 *
	 * @param konto        konto użytkownika
	 * @param walutaZ      waluta, z której wykonujemy przeliczenie
	 * @param WalutaDo     waluta, z której wykonujemy przeliczenie
	 * @param kwotaWymiany kwota do wymiany
	 * @return zaktualizowane konto użytkownika
	 */
	public Konto przeliczWalute(Konto konto, Waluta walutaZ, Waluta WalutaDo, BigDecimal kwotaWymiany) {

		var weryfikacjaPrzeliczenia = weryfikujMozliwoscPrzeliczeniaWaluty(konto, walutaZ, WalutaDo, kwotaWymiany);
		if (weryfikacjaPrzeliczenia.czyWeryfikacjaZBledem()) {
			throw new PrzeliczenieWalutyException(weryfikacjaPrzeliczenia.komunikatBledu());
		}

		return wykonajPrzeliczenieWaluty(konto, walutaZ, WalutaDo, kwotaWymiany);
	}

	private WeryfikacjaPrzeliczenia weryfikujMozliwoscPrzeliczeniaWaluty(Konto konto, Waluta walutaZ, Waluta walutaDo, BigDecimal kwotaWymiany) {
		return new WeryfikacjaPrzeliczenia()
				.weryfikujIstnienieSaldaDlaWaluty(konto.getSalda(), walutaZ)
				.weryfikujIstnienieSaldaDlaWaluty(konto.getSalda(), walutaDo)
				.weryfikujStanKonta(konto.getSalda(), walutaZ, kwotaWymiany);
	}


	//TODO: @Transactional
	private Konto wykonajPrzeliczenieWaluty(Konto konto, Waluta walutaZ, Waluta walutaDo, BigDecimal kwotaWymiany) {

		var saldoZ = odczytajSaldoZKonta(konto, walutaZ);
		saldoZ.setSaldoKonta(saldoZ.getSaldoKonta().subtract(kwotaWymiany));

		var saldoDo = odczytajSaldoZKonta(konto, walutaDo);
		var walutaOdczytu = odczytajWaluteObca(walutaZ, walutaDo);
		var kierunekPrzeliczenia = odczytajKierunekPrzeliczania(walutaZ);
		BigDecimal pobranyKursWaluty = odczytKursuWalutyService.odczytajKursWaluty(walutaOdczytu, kierunekPrzeliczenia);
		var przeliczoneSrodkiPoWymianie = kwotaWymiany.multiply(pobranyKursWaluty);
		saldoDo.setSaldoKonta(saldoDo.getSaldoKonta().add(przeliczoneSrodkiPoWymianie));

		saldoRepository.save(saldoZ);
		saldoRepository.save(saldoDo);
		return konto;
	}

	private Saldo odczytajSaldoZKonta(Konto konto, Waluta waluta) {
		return konto.getSalda().stream().filter(s -> s.getWaluta().equals(waluta)).findFirst().get();
	}
	private Waluta odczytajWaluteObca(Waluta walutaZ, Waluta walutaDo) {
		return Waluta.ZLOTY == walutaZ ? walutaDo : walutaZ;
	}
	private KierunekPrzeliczania odczytajKierunekPrzeliczania(Waluta walutaZ) {
		return Waluta.ZLOTY == walutaZ ? KierunekPrzeliczania.SPRZEDAZ : KierunekPrzeliczania.ZAKUP;
	}
}
