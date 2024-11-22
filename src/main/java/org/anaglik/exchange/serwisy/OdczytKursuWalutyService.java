package org.anaglik.exchange.serwisy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.konfiguracje.TechniczneParametryKonfiguracji;
import org.anaglik.exchange.wyjatki.OdczytKursuWalutyException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * Klasa realizuje odczyt aktualnego kursu waluty z API NBP.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class OdczytKursuWalutyService {

	private static final String TABELA_KURSOW = "C";
	private static final String POLE_PRZELICZONY_KURS_SPRZEDAZY_WALUTY = "ask";
	private static final String NAZWA_TABLICY_KURSU_SPRZEDAZY_WALUTY = "rates";

	private final RestTemplate restTemplate;
	private final TechniczneParametryKonfiguracji systemConfig;

	/**
	 * NBP Web API oferuje 3 tabele (A, B, C) kursów walut obcych.
	 * Jedynie tabela C udostępnia kurs kupna walut obcych.
	 */
	public BigDecimal odczytajKursWaluty(Waluta przeliczanaWaluta) {
		log.debug("Pobieram z NBP aktualny kurs kupna dla waluty: {}", przeliczanaWaluta);
		try {
			String endpointUslugi = systemConfig.getAdresPobranejAktualnejTabeliKursow();
			ResponseEntity<String> odpowiedz = restTemplate.getForEntity(endpointUslugi, String.class, TABELA_KURSOW, przeliczanaWaluta.getKodWaluty());

			if (odpowiedz.getStatusCode() == HttpStatus.OK) {
				return odczytajKursWalutyZObjektuJson(odpowiedz.getBody());
			}
			throw new OdczytKursuWalutyException(String.format("Nieoczekiwana odpowiedz z NPB, kod odpowiedzi: %s", odpowiedz.getStatusCode().value()));
		} catch (RestClientException ex) {
			log.error("Blad odczytu danych z NBP", ex);
			throw new OdczytKursuWalutyException("Blad odczytu danych z NBP", ex);
		}
	}

	private BigDecimal odczytajKursWalutyZObjektuJson(String json) {
		double aktualnyKurs;

		try {
			var jsonObject = new JSONObject(json);
			var odczytanaTablicaZKurasamiWaluty = (JSONObject) jsonObject.getJSONArray(NAZWA_TABLICY_KURSU_SPRZEDAZY_WALUTY).get(0);
			aktualnyKurs = odczytanaTablicaZKurasamiWaluty.getDouble(POLE_PRZELICZONY_KURS_SPRZEDAZY_WALUTY);
		} catch (JSONException ex) {
			log.error("Blad konwersji JSON", ex);
			throw new OdczytKursuWalutyException("Blad konwersji JSON", ex);
		}
		return BigDecimal.valueOf(aktualnyKurs);
	}
}
