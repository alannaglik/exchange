package org.anaglik.exchange.serwisy;

import lombok.AllArgsConstructor;
import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.modele.Konto;
import org.anaglik.exchange.repozytoria.KontoRepository;
import org.anaglik.exchange.wyjatki.OdczytKontaUzytkownikaException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class KontoService {

	private final KontoRepository kontoRepository;
	private final PrzeliczenieWalutyService przeliczenieWalutyService;

	public Long utworzKonto(Konto konto) {
		final Konto zapisaneKonto = kontoRepository.save(konto);
		return zapisaneKonto.getIdentyfikatorKonta();
	}

	public Long zwrocLiczbeKontUzytkownikow() {
		return kontoRepository.count();
	}

	public Optional<Konto> pobierzKontoUzytkownika(long idKonta) {
		return kontoRepository.findById(idKonta);
	}

	public void usunKontoUzytkownika(Konto konto) {
		long idKonta = konto.getIdentyfikatorKonta();
		Optional<Konto> kontoDoUsuniecia = kontoRepository.findById(idKonta);
		kontoDoUsuniecia.ifPresent(kontoRepository::delete);
	}

	public Konto aktualizujKontoDlaPrzeliczeniaWaluty(long idKonta, Waluta waluta) {
		Optional<Konto> konto = pobierzKontoUzytkownika(idKonta);
		if (!konto.isPresent()) {
			throw new OdczytKontaUzytkownikaException("Wyszukiwane konto uzytkownika o id: " + idKonta + " nie istnieje");
		}
		return przeliczenieWalutyService.przeliczWalute(konto.get(), waluta);
	}
}
