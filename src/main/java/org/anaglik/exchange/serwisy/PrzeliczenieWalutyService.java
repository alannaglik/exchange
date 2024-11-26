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
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Klasa realizuje przeliczenie waluty dla konta użytkownika.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class PrzeliczenieWalutyService {

	private final OdczytKursuWalutyService odczytKursuWalutyService;
	private final SaldoRepository saldoRepository;
	private final WeryfikacjaPrzeliczenia weryfikacjaPrzeliczenia;


	/**
	 * Metoda realizuje przeliczenie dla podanej waluty
	 *
	 * @param konto        konto użytkownika
	 * @param walutaZ      waluta, z której wykonujemy przeliczenie
	 * @param walutaDo     waluta, z której wykonujemy przeliczenie
	 * @param kwotaWymiany kwota do wymiany
	 * @return zaktualizowane konto użytkownika
	 */
	public Konto przeliczWalute(Konto konto, Waluta walutaZ, Waluta walutaDo, BigDecimal kwotaWymiany) {

		var weryfikacjaPrzeliczenia = weryfikujMozliwoscPrzeliczeniaWaluty(konto, walutaZ, walutaDo, kwotaWymiany);
		if (weryfikacjaPrzeliczenia.czyWeryfikacjaZBledem()) {
			throw new PrzeliczenieWalutyException(weryfikacjaPrzeliczenia.komunikatBledu());
		}

		return wykonajPrzeliczenieWaluty(konto, walutaZ, walutaDo, kwotaWymiany);
	}

	private WeryfikacjaPrzeliczenia weryfikujMozliwoscPrzeliczeniaWaluty(Konto konto, Waluta walutaZ, Waluta walutaDo, BigDecimal kwotaWymiany) {
		return weryfikacjaPrzeliczenia.utworz()
				.weryfikujStanKwotyWymiany(kwotaWymiany)
				.weryfikujIstnienieSaldaDlaWaluty(konto.getSalda(), walutaZ)
				.weryfikujIstnienieSaldaDlaWaluty(konto.getSalda(), walutaDo)
				.weryfikujStanKonta(konto.getSalda(), walutaZ, kwotaWymiany);
	}


	//TODO: @Transactional
	private Konto wykonajPrzeliczenieWaluty(Konto konto, Waluta walutaZ, Waluta walutaDo, BigDecimal kwotaWymiany) {

		//Przeliczam saldoZ
		var saldoZ = odczytajSaldoZKonta(konto, walutaZ);
		saldoZ.setSaldoKonta(saldoZ.getSaldoKonta().subtract(kwotaWymiany));

		//Przeliczam saldoDo
		var saldoDo = odczytajSaldoZKonta(konto, walutaDo);
		var walutaOdczytu = odczytajWaluteObca(walutaZ, walutaDo);
		if (walutaZ == Waluta.ZLOTY) {
			var pobranyKursWaluty = odczytKursuWalutyService.odczytajKursWaluty(walutaOdczytu, KierunekPrzeliczania.SPRZEDAZ);
			var przeliczoneSrodkiPoWymianie = kwotaWymiany.divide(pobranyKursWaluty, RoundingMode.HALF_UP);
			saldoDo.setSaldoKonta(saldoDo.getSaldoKonta().add(przeliczoneSrodkiPoWymianie));
		} else {
			var pobranyKursWaluty = odczytKursuWalutyService.odczytajKursWaluty(walutaOdczytu, KierunekPrzeliczania.ZAKUP);
			var przeliczoneSrodkiPoWymianie = kwotaWymiany.multiply(pobranyKursWaluty);
			saldoDo.setSaldoKonta(saldoDo.getSaldoKonta().add(przeliczoneSrodkiPoWymianie));
		}

		saldoRepository.save(saldoZ);
		saldoRepository.save(saldoDo);
		return konto;
	}


	private static Saldo odczytajSaldoZKonta(Konto konto, Waluta waluta) {
		return konto.getSalda().stream().filter(s -> s.getWaluta() == waluta).findFirst().get();
	}

	private static Waluta odczytajWaluteObca(Waluta walutaZ, Waluta walutaDo) {
		if (Waluta.ZLOTY == walutaZ) return walutaDo;
		return walutaZ;
	}
}
