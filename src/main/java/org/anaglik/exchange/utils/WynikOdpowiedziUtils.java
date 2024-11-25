package org.anaglik.exchange.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.anaglik.exchange.enumy.DefinicjeBledu;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.text.MessageFormat;

public class WynikOdpowiedziUtils {

	public static ResponseEntity bladParametruWywolania(BindingResult bindingResult) {
		var definicjeBledu = DefinicjeBledu.NIEPORAWNY_PARAMETR_ZAPYTANIA;
		var blad = bindingResult.getFieldErrors().stream().findFirst().get();
		var wyjatekDto = new WyjatekDto(definicjeBledu.getOpisBledu(), MessageFormat.format("Blad w polu {0} - {1}", blad.getField(), blad.getDefaultMessage()));
		return new ResponseEntity<>(wyjatekDto, definicjeBledu.getStatusHttp());
	}

	@Getter
	@AllArgsConstructor
	static class WyjatekDto {
		private String opisBledu;
		private String definicjaBledu;
	}
}
