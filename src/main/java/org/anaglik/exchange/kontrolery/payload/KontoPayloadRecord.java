package org.anaglik.exchange.kontrolery.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Klasa dla walidacji danych użytkownika konta.
 */

public record KontoPayloadRecord(@NotEmpty(message = "Imię jest wymagane") String imie,
								 @NotEmpty(message = "Nazwisko jest wymagane") String nazwisko,
								 @NotNull(message = "Saldo konta jest wymagane") double saldoKontaPLN) {
}
