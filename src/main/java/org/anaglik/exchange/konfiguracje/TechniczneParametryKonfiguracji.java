package org.anaglik.exchange.konfiguracje;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Klasa odpowiedzialna za techniczne parametry konfiguracji systemu.
 */

@Component
@ConfigurationProperties(prefix = "config")
@Getter
@Setter
public class TechniczneParametryKonfiguracji {

    private String adresPobranejAktualnejTabeliKursow;

}
