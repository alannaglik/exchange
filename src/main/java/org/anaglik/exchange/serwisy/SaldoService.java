package org.anaglik.exchange.serwisy;

import lombok.AllArgsConstructor;
import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.modele.Konto;
import org.anaglik.exchange.modele.Saldo;
import org.anaglik.exchange.repozytoria.SaldoRepository;
import org.anaglik.exchange.wyjatki.OdczytKontaUzytkownikaException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SaldoService {

	private final KontoService kontoService;
	private final PrzeliczenieWalutyService przeliczenieWalutyService;
	private final SaldoRepository saldoRepository;

	public void utworzSalda(List<Saldo> salda) {
		saldoRepository.saveAll(salda);
	}

	public Konto aktualizujKontoDlaPrzeliczeniaWaluty(long idKonta, Waluta walutaZ, Waluta WalutaDo, BigDecimal kwotaWymiany) {
		Optional<Konto> konto = kontoService.pobierzKontoUrzytkownikaIJegoSalda(idKonta);
		if (!konto.isPresent()) {
			throw new OdczytKontaUzytkownikaException("Wyszukiwane konto uzytkownika o id: " + idKonta + " nie istnieje");
		}
		return przeliczenieWalutyService.przeliczWalute(konto.get(), walutaZ, WalutaDo, kwotaWymiany);
	}

}
