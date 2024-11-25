package org.anaglik.exchange.utils;

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

/**
 * Klasa pomocnicza dla obsługi BigDecimal.
 */

@UtilityClass
public class BigDecimalUtils {
    public BigDecimalWarunek czy(BigDecimal wartosc) {
        return new BigDecimalWarunek(wartosc);
    }

	/**
	 * Klasa wewnętrzna dla warunków porównań BigDecimal.
	 */
    @AllArgsConstructor
    public class BigDecimalWarunek {
        private BigDecimal wartosc;

        public boolean jestWiekszeOd(BigDecimal wartoscDoPorownania) {
            return wartosc != null && wartosc.compareTo(wartoscDoPorownania) > 0;
        }

		/**
		 * Metoda porównuje dwie zmienne poprzez compareTo.
		 * W przypadku dwóch wartości null zwraca true.
		 *
		 * @param wartoscDoPorownania wartość do porównania
		 * @return czy wartości równe
		 */
		public boolean jestRowne(BigDecimal wartoscDoPorownania) {
			if (wartosc == null && wartoscDoPorownania == null) {
				return true;
			}
			return wartosc != null && wartosc.compareTo(wartoscDoPorownania) == 0;
		}
    }
}
