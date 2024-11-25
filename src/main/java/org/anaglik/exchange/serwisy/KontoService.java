package org.anaglik.exchange.serwisy;

import lombok.AllArgsConstructor;
import org.anaglik.exchange.modele.Konto;
import org.anaglik.exchange.repozytoria.KontoRepository;
import org.anaglik.exchange.repozytoria.SaldoRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class KontoService {

	private final KontoRepository kontoRepository;
	private final SaldoRepository saldoRepository;

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

	public Optional<Konto> pobierzKontoUrzytkownikaIJegoSalda(long idKonta) {
		return Optional.ofNullable(kontoRepository.pobierzKontoUrzytkownikaIJegoSalda(idKonta));
	}

	public void usunKontoUzytkownika(Konto konto) {
		long idKonta = konto.getIdentyfikatorKonta();
		Optional<Konto> kontoDoUsuniecia = kontoRepository.findById(idKonta);
		if (kontoDoUsuniecia.isPresent()) {
			if (CollectionUtils.isNotEmpty(kontoDoUsuniecia.get().getSalda())) {
				saldoRepository.deleteAll();
			}
			kontoRepository.delete(kontoDoUsuniecia.get());
		}
	}
}
