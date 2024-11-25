package org.anaglik.exchange.kontrolery.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.PathVariable;

public record SaldoPayloadRecord(
		@PathVariable @NotNull(message = "Identyfikator konta jest wymagany") long identyfikatorKonta,
		@PathVariable @NotEmpty(message = "Kod waluty jest wymagany") @Size(min = 3, max = 3, message = "Kod waluty musi zawierać 3 znaki") String kodWaluty,
		@PathVariable @NotNull(message = "Kwota wymiany jest wymagana") double kwota) {
}
