package org.anaglik.exchange.utils;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class BigDecimalUtilsTest {

	@DataProvider
	public Object[][] wiekszeDataProvider() {
		return new Object[][]{
				{BigDecimal.ONE, BigDecimal.ONE, false},
				{BigDecimal.ONE, BigDecimal.TEN, false},
				{BigDecimal.TEN, BigDecimal.ONE, true},
				{null, BigDecimal.ONE, false}
		};
	}

	@Test(dataProvider = "wiekszeDataProvider")
	public void powinienPoprawnieZweryfikowacRelacjeWieksze(BigDecimal pierwszaWartosc, BigDecimal drugaWartosc, boolean spodziewanyWynik) {
		//then
		boolean wynik = BigDecimalUtils.czy(pierwszaWartosc).jestWiekszeOd(drugaWartosc);

		//then
		Assertions.assertThat(wynik).isEqualTo(spodziewanyWynik);
	}
}
