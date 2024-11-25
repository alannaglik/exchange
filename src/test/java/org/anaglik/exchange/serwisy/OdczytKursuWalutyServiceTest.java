package org.anaglik.exchange.serwisy;

import org.anaglik.exchange.enumy.Waluta;
import org.anaglik.exchange.konfiguracje.TechniczneParametryKonfiguracji;
import org.anaglik.exchange.wyjatki.OdczytKursuWalutyException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class OdczytKursuWalutyServiceTest {

	private static final String ENDPOINT_USLUGI = "http://api.nbp.pl/api/exchangerates/rates/{table}/{code}/";
	private static final String TABELA_KURSOW = "C";
	public static final String ODPOWIEDZ_JSON = """
			   {
			   	"table": "C",
			   	"currency": "dolar amerykański",
			   	"code": "USD",
			   	"rates": [{
			           "no": "226/C/NBP/2024",
			           "effectiveDate": "2024-11-21",
			           "bid": 4.0845,
			           "ask": 4.1671
			       	}]
			}""";


	@Mock
	private RestTemplate restTemplate;
	@Mock
	private TechniczneParametryKonfiguracji systemConfig;

	private OdczytKursuWalutyService sut;

	@BeforeMethod
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
		sut = new OdczytKursuWalutyService(restTemplate, systemConfig);
	}

	@Test
	public void powinienPoprawienOdczytacKursWaluty() {
		//given
		Waluta przeliczanaWaluta = Waluta.DOLAR_AMERYKANSKI;
		ResponseEntity<String> oczekiwanaOdpowiedz = przygotujOdpowiedz(ODPOWIEDZ_JSON);

		when(systemConfig.getAdresPobranejAktualnejTabeliKursow()).thenReturn(ENDPOINT_USLUGI);
		when(restTemplate.getForEntity(eq(ENDPOINT_USLUGI), any(Class.class), eq(TABELA_KURSOW), eq(przeliczanaWaluta.getKodWaluty()))).thenReturn(oczekiwanaOdpowiedz);

		//when
		final BigDecimal wynik = sut.odczytajKursWaluty(przeliczanaWaluta);

		//then
		assertThat(wynik).isNotNull()
				.isEqualTo(BigDecimal.valueOf(4.1671));

	}

	@Test(expectedExceptions = OdczytKursuWalutyException.class)
	public void powinienZwrocicWyjatekDlaNieporawnejOdpowiedzi() {
		//given
		Waluta przeliczanaWaluta = Waluta.DOLAR_AMERYKANSKI;

		when(systemConfig.getAdresPobranejAktualnejTabeliKursow()).thenReturn(ENDPOINT_USLUGI);
		when(restTemplate.getForEntity(eq(ENDPOINT_USLUGI), any(Class.class), eq(TABELA_KURSOW), eq(przeliczanaWaluta.getKodWaluty()))).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

		//when
		sut.odczytajKursWaluty(przeliczanaWaluta);

		//then OdczytKursuWalutyException
	}

	@Test(expectedExceptions = OdczytKursuWalutyException.class)
	public void powinienZwrocicWyjatekDlaBleduKonwersjiJson() {
		//given
		Waluta przeliczanaWaluta = Waluta.DOLAR_AMERYKANSKI;
		ResponseEntity<String> oczekiwanaOdpowiedz = przygotujOdpowiedz("{}");

		when(systemConfig.getAdresPobranejAktualnejTabeliKursow()).thenReturn(ENDPOINT_USLUGI);
		when(restTemplate.getForEntity(eq(ENDPOINT_USLUGI), any(Class.class), eq(TABELA_KURSOW), eq(przeliczanaWaluta.getKodWaluty()))).thenReturn(oczekiwanaOdpowiedz);

		//when
		sut.odczytajKursWaluty(przeliczanaWaluta);

		//then OdczytKursuWalutyException
	}

	@Test(expectedExceptions = OdczytKursuWalutyException.class)
	public void powinienZwrocicWyjatejDlaBleduKonwersjiJson() {
		//given
		Waluta przeliczanaWaluta = Waluta.DOLAR_AMERYKANSKI;

		when(systemConfig.getAdresPobranejAktualnejTabeliKursow()).thenReturn(ENDPOINT_USLUGI);
		when(restTemplate.getForEntity(eq(ENDPOINT_USLUGI), any(Class.class), eq(TABELA_KURSOW), eq(przeliczanaWaluta.getKodWaluty()))).thenThrow(new OdczytKursuWalutyException("Blad konwersji JSON"));

		//when
		sut.odczytajKursWaluty(przeliczanaWaluta);

		//then OdczytKursuWalutyException
	}

	@Test(expectedExceptions = OdczytKursuWalutyException.class)
	public void powinienZwrocicWyjatejDlaBleduOdczytuDanych() {
		//given
		Waluta przeliczanaWaluta = Waluta.DOLAR_AMERYKANSKI;

		when(systemConfig.getAdresPobranejAktualnejTabeliKursow()).thenReturn(ENDPOINT_USLUGI);
		when(restTemplate.getForEntity(eq(ENDPOINT_USLUGI), any(Class.class), eq(TABELA_KURSOW), eq(przeliczanaWaluta.getKodWaluty()))).thenThrow(new RestClientException("msg"));

		//when
		sut.odczytajKursWaluty(przeliczanaWaluta);

		//then OdczytKursuWalutyException
	}

	private static ResponseEntity<String> przygotujOdpowiedz(String body) {
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
}
