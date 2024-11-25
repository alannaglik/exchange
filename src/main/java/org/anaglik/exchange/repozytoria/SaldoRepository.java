package org.anaglik.exchange.repozytoria;

import org.anaglik.exchange.modele.Saldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Dostęp do danych w bazie dotyczących salda konta użytkownika
 */
@Repository
public interface SaldoRepository extends JpaRepository<Saldo, Long> {

}
