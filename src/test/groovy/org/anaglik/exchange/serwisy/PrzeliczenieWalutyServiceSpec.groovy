package org.anaglik.exchange.serwisy

import org.anaglik.exchange.enumy.KierunekPrzeliczania
import org.anaglik.exchange.enumy.Waluta
import org.anaglik.exchange.modele.Konto
import org.anaglik.exchange.modele.Saldo
import org.anaglik.exchange.przeliczenie.waluty.weryfikacja.WeryfikacjaPrzeliczenia
import org.anaglik.exchange.repozytoria.SaldoRepository
import org.anaglik.exchange.wyjatki.PrzeliczenieWalutyException
import spock.lang.Specification

class PrzeliczenieWalutyServiceSpec extends Specification {

    def odczytKursuWalutyService = Mock(OdczytKursuWalutyService)
    def saldoRepository = Mock(SaldoRepository)
    def weryfikacjaPrzeliczenia = Mock(WeryfikacjaPrzeliczenia)

    def sut = new PrzeliczenieWalutyService(odczytKursuWalutyService, saldoRepository, weryfikacjaPrzeliczenia)

    def "powinien poprawnie przeliczyc walute na USD"() {
        given:
            def saldoPLN = przygotujSaldo(1L, BigDecimal.TEN, Waluta.ZLOTY)
            def saldoUSD = przygotujSaldo(2L, BigDecimal.TWO, Waluta.DOLAR_AMERYKANSKI)
            def konto = przygotujKonto(saldoPLN, saldoUSD)
            def walutaZ = Waluta.ZLOTY
            def walutaDo = Waluta.DOLAR_AMERYKANSKI
            def kwotaWymiany = BigDecimal.ONE
            def weryfikacja = new WeryfikacjaPrzeliczenia()

            weryfikacjaPrzeliczenia.utworz() >> weryfikacja
            odczytKursuWalutyService.odczytajKursWaluty(Waluta.DOLAR_AMERYKANSKI, KierunekPrzeliczania.SPRZEDAZ) >> BigDecimal.valueOf(4L)

        when:
            def wynik = sut.przeliczWalute(konto, walutaZ, walutaDo, kwotaWymiany)

        then:
            wynik != null
            def odczytaneSaldoPLN = odczytajSaldoPoId(wynik, 1L)
            odczytaneSaldoPLN.saldoKonta.equals(BigDecimal.valueOf(9.0))

            def odczytaneSaldoUSD = odczytajSaldoPoId(wynik, 2L)
            odczytaneSaldoUSD.saldoKonta.equals(BigDecimal.valueOf(2.25))
    }

    def "powinien poprawnie przeliczyc walute na PLN"() {
        given:
            def saldoPLN = przygotujSaldo(1L, BigDecimal.TEN, Waluta.ZLOTY)
            def saldoUSD = przygotujSaldo(2L, BigDecimal.TWO, Waluta.DOLAR_AMERYKANSKI)
            def konto = przygotujKonto(saldoPLN, saldoUSD)
            def walutaZ = Waluta.DOLAR_AMERYKANSKI
            def walutaDo = Waluta.ZLOTY
            def kwotaWymiany = BigDecimal.ONE
            def weryfikacja = new WeryfikacjaPrzeliczenia()

            weryfikacjaPrzeliczenia.utworz() >> weryfikacja
            odczytKursuWalutyService.odczytajKursWaluty(Waluta.DOLAR_AMERYKANSKI, KierunekPrzeliczania.ZAKUP) >> BigDecimal.valueOf(4L)

        when:
            def wynik = sut.przeliczWalute(konto, walutaZ, walutaDo, kwotaWymiany)

        then:
            wynik != null
            def odczytaneSaldoPLN = odczytajSaldoPoId(wynik, 1L)
            odczytaneSaldoPLN.saldoKonta.equals(BigDecimal.valueOf(14.0))

            def odczytaneSaldoUSD = odczytajSaldoPoId(wynik, 2L)
            odczytaneSaldoUSD.saldoKonta.equals(BigDecimal.valueOf(1.0))

            2 * saldoRepository.save(_ as Saldo)
    }

    def "powinien zglosic wyjatek dla bledu weryfikacji przeliczenia waluty jesli brak drugiego salda"() {
        given:
            def saldoPLN = przygotujSaldo(1L, BigDecimal.TEN, Waluta.ZLOTY)
            def konto = przygotujKonto(saldoPLN)
            def walutaZ = Waluta.DOLAR_AMERYKANSKI
            def walutaDo = Waluta.ZLOTY
            def kwotaWymiany = BigDecimal.ONE
            def weryfikacja = new WeryfikacjaPrzeliczenia()

            weryfikacjaPrzeliczenia.utworz() >> weryfikacja

        when:
            sut.przeliczWalute(konto, walutaZ, walutaDo, kwotaWymiany)

        then:
            def ex = thrown PrzeliczenieWalutyException
            ex.message == 'Blad weryfikacji przeliczenia. Uzytkownik nie posiada konta z obsluga waluty: Stany Zjednoczonone'
    }

    private static Konto przygotujKonto(Saldo... s) {
        return Konto.builder()
                .identyfikatorKonta(1L)
                .imie('Jan')
                .nazwisko('Kowalski')
                .salda(Arrays.asList(s)).build()
    }

    private static Saldo przygotujSaldo(long id, double saldoKonta, Waluta waluta) {
        return Saldo.builder()
                .identyfikatorSalda(id)
                .saldoKonta(BigDecimal.valueOf(saldoKonta))
                .waluta(waluta).build()
    }

    private Saldo odczytajSaldoPoId(Konto konto, Long id) {
        return konto.salda.stream().filter(s -> s.identyfikatorSalda == id).findFirst().get()
    }
}
