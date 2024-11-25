package org.anaglik.exchange.repozytoria;

import org.anaglik.exchange.modele.Konto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

/**
 * Dostęp do danych w bazie dotyczących konta użytkownika
 */
@Repository
public interface KontoRepository extends JpaRepository<Konto, Long> {

	@Query(value = "SELECT k.* FROM KONTO AS k " +
			"JOIN SALDO AS s ON k.id = s.konto_id " +
			"WHERE k.id = :idKontaUzytkownika;", nativeQuery = true)
	Konto pobierzKontoUrzytkownikaIJegoSalda(@Param("idKontaUzytkownika") long idKontaUzytkownika);

}
