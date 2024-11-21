package org.anaglik.exchange.repozytoria;

import org.anaglik.exchange.modele.Konto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Dostęp do danych w bazie dotyczących konta użytkownika
 */
@Repository
public interface KontoRepository extends JpaRepository<Konto, Long> {
}
