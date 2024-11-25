package org.anaglik.exchange.utils;

import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.modele.Konto;
import org.anaglik.exchange.modele.Saldo;

import java.math.BigDecimal;

public class SaldoUtils {

	public static Saldo utworzSaldoPln(double saldo, Konto konto) {
		return Saldo.builder().saldoKonta(BigDecimal.valueOf(saldo)).waluta(Waluta.ZLOTY).konto(konto).build();
	}
	public static Saldo utworzSaldoUsd(double saldo, Konto konto) {
		return Saldo.builder().saldoKonta(BigDecimal.valueOf(saldo)).waluta(Waluta.DOLAR_AMERYKANSKI).konto(konto).build();
	}
}
