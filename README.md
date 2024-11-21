# Opis działania aplikacji extange
* Autor: Alan Naglik
* Data: 22.11.2024

# Funkcjonalność aplikacji:
* udostępnia REST API dla obsługi kont użytkowników. 
* umożliwia wymianę pieniędzy pomiędzy PLN <-> USD wg aktualnego kursu wymiany NBP (http://api.nbp.pl/)
* zapisuje stan danych do bazy H2

# REST API aplikacji
## Utworzenie konta użytkownika:
curl -X POST --location "http://localhost:8080/api/konta" 
\ -H "Content-Type: application/json" 
\ -d '{ "imie": "Jan", "nazwisko": "Kowalski", "saldoKontaPLN": 100 }'

Walidowane są wymagane pola: imie, nazwisko, saldoKontaPLN

## Pobranie konta użytkownika:
curl -X GET --location "http://localhost:8080/api/konta/{{identyfikatorKonta}}
Uwaga: należy podać konkrety identyfikator użytkownika dla {{identyfikatorKonta}}

## Usunięcie konta użytkownika:
curl -X DELETE --location "http://localhost:8080/api/konta/{{identyfikatorKonta}}"
Uwaga: należy podać konkrety identyfikator użytkownika dla {{identyfikatorKonta}}

## Pobranie liczby kont użytkowników:
curl -X GET --location "http://localhost:8080/api/konta"

## Aktualizacja dla przeliczenia waluty konta użytkownika:
curl -X PUT --location "http://localhost:8080/api/konta/{{identyfikatorKonta}}/przeliczanaWaluta/{kodWaluty}"
Uwaga: należy podać konkrety identyfikator użytkownika dla {{identyfikatorKonta}} oraz trzyliterowy kod waluty {{kodWaluty}}

## Pobranie aktualnej wersji API:
curl -X GET --location http://localhost:8080/api/konta/api


# Baza danych H2
Dane zapisywane są przez aplikację do pamięciowej bazy danych H2. 
Po uruchomieniu aplikacji mamy dostęp konsoli H2 w przeglądarce pod adresem http://localhost:8080/konsola-h2/

Dane do zalogowania w konsoli H2:

* Datasource: jdbc:h2:mem:exchange
* Użytkownik: user
* Hasło: xxx

