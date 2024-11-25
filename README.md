# Opis działania aplikacji extange
* Autor: Alan Naglik
* Data: 22.11.2024

# Funkcjonalność aplikacji:
* udostępnia REST API dla obsługi kont użytkowników. 
* umożliwia wymianę pieniędzy pomiędzy PLN <-> USD, EUR wg aktualnego kursu wymiany NBP (http://api.nbp.pl/)
* zapisuje stan danych do bazy H2 (z użyciem liquidbase)

# REST API aplikacji
## Utworzenie konta użytkownika:
Endpoint tworzy konto użytkownika wraz dwoma saldami: PLN i USD

curl -X POST --location "http://localhost:8080/api/konta" 
\ -H "Content-Type: application/json" 
\ -d '{ "imie": "Jan", "nazwisko": "Kowalski", "saldoKontaPLN": 100 }'

Dopuszczalne są pola: 
- imie (pole obligatoryjne),
- nazwisko (pole obligatoryjne), 
- saldoKontaPLN (pole obligatoryjne - zostanie utworzone saldo konta w PLN z wartoscią pola saldoKontaPLN),
- saldoKontaUSD (pole opcjonalne - zostanie utworzone saldo konta w USD z wartoscią pola saldoKontaUSD lub 0.0 jesli nie podano)



## Pobranie konta użytkownika:
Endpoint odczytuje konto o identyfikatorze użytkownika {{identyfikatorKonta}} wraz saldami

curl -X GET --location "http://localhost:8080/api/konta/{{identyfikatorKonta}}

Uwaga: należy podać konkrety identyfikator użytkownika dla {{identyfikatorKonta}}

## Usunięcie konta użytkownika:
curl -X DELETE --location "http://localhost:8080/api/konta/{{identyfikatorKonta}}"

Uwaga: należy podać konkrety identyfikator użytkownika dla {{identyfikatorKonta}}

## Pobranie liczby kont użytkowników:
Endpoint zwraca liczbę kont użytkowników

curl -X GET --location "http://localhost:8080/api/konta"

## Aktualizacja dla przeliczenia waluty konta użytkownika z waluty obcej {kodWaluty} do PLN:
Endpoint przelicza z obcej waluty na PLN

curl -X PUT --location "http://localhost:8080/api/konta/{{identyfikatorKonta}}/przeliczDoPln/{kodWaluty}/{kwota}"

Uwaga: należy podać:
- identyfikator użytkownika dla {{identyfikatorKonta}} 
- trzyliterowy kod waluty {{kodWaluty}} - dopuszczalne są waluty USD, EUR
- kwotę do przeliczenia

## Aktualizacja dla przeliczenia waluty konta użytkownika z PLN do waluty obcej {kodWaluty}:
Endpoint przelicza z PLN do waluty obcej {kodWaluty}

curl -X PUT --location "http://localhost:8080/api/konta/{{identyfikatorKonta}}/przeliczZPln/{kodWaluty}/{kwota}"

Uwaga: należy podać:
- identyfikator użytkownika dla {{identyfikatorKonta}}
- trzyliterowy kod waluty {{kodWaluty}} - dopuszczalne są waluty USD, EUR
- kwotę do przeliczenia



# Baza danych H2
Dane zapisywane są przez aplikację do pamięciowej bazy danych H2.

Po uruchomieniu aplikacji mamy dostęp konsoli H2 w przeglądarce pod adresem http://localhost:8080/konsola-h2/

Po uruchomieniu do bazy zapisywane jest konto testowe wraz z trzema saldami (PLN, USD, EUR)

Dane do zalogowania w konsoli H2:

* Datasource: jdbc:h2:mem:exchange
* Użytkownik: user
* Hasło: xxx

