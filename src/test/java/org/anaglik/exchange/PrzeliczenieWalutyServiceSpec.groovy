package org.anaglik.exchange

import org.anaglik.exchange.enumy.Waluta
import org.anaglik.exchange.modele.Konto

import org.anaglik.exchange.repozytoria.KontoRepository
import org.anaglik.exchange.serwisy.PrzeliczenieWalutyService
import org.anaglik.exchange.wyjatki.PrzeliczenieWalutyException
import spock.lang.Specification

class PrzeliczenieWalutyServiceSpec extends Specification {

    def kontoRepository = Mock(KontoRepository)
    def przeliczenieWalutyDoPLN = Mock(PrzeliczenieWalutyDoPLN)
    def przeliczenieWalutyDoUSD = Mock(PrzeliczenieWalutyDoUSD)

    def sut = new PrzeliczenieWalutyService(kontoRepository, przeliczenieWalutyDoPLN, przeliczenieWalutyDoUSD)

    def "powinien poprawnie przeliczyc walute na USD"() {
        given:
            def konto = Konto.builder().build()
            def zaktualizowneKonto = Konto.builder().build()
            def waluta = Waluta.DOLAR_AMERYKANSKI

            przeliczenieWalutyDoUSD.weryfikujMozliwoscPrzeliczeniaWaluty(konto) >> true
            przeliczenieWalutyDoUSD.wykonajPrzeliczenieWaluty(konto, waluta) >> zaktualizowneKonto

        when:
            def wynik = sut.przeliczWalute(konto, waluta)

        then:
            wynik == zaktualizowneKonto
            1 * kontoRepository.save(zaktualizowneKonto)
            0 * przeliczenieWalutyDoPLN._
    }

    def "powinien poprawnie przeliczyc walute na PLN"() {
        given:
            def konto = Konto.builder().build()
            def zaktualizowneKonto = Konto.builder().build()
            def waluta = Waluta.ZLOTY

            przeliczenieWalutyDoPLN.weryfikujMozliwoscPrzeliczeniaWaluty(konto) >> true
            przeliczenieWalutyDoPLN.wykonajPrzeliczenieWaluty(konto, waluta) >> zaktualizowneKonto

        when:
            def wynik = sut.przeliczWalute(konto, waluta)

        then:
            wynik == zaktualizowneKonto
            1 * kontoRepository.save(zaktualizowneKonto)
            0 * przeliczenieWalutyDoUSD._
    }

    def "powinien zglosic wyjatek dla bledu weryfikacji przeliczenia waluty"() {
        given:
            def konto = Konto.builder().build()
            def waluta = Waluta.DOLAR_AMERYKANSKI

            przeliczenieWalutyDoUSD.weryfikujMozliwoscPrzeliczeniaWaluty(konto) >> false

        when:
            sut.przeliczWalute(konto, waluta)

        then:
            def ex = thrown PrzeliczenieWalutyException
            ex.message == 'Blad weryfikacji dla przeliczenia'
    }
}
