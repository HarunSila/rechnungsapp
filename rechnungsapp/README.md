# Backend

Dieses Projekt ist das Backend für die Rechnungsapp, ein Projekt, welches im Rahmen der semesterbegleitenden Projektarbeit im Modul Java EE an der FH Südwestfalen erstellt wurde.

Die funktionalen Anforderungen, sowie Archtiektur und Design sind der Aufgabenstellung und Dokumentation zu entnehmen.

## Voraussetzungen
1. VS Code (oder andere IDE, welche geeignet für Java Spring Boot Applikationen ist)
2. Java 21
3. Maven 3.9.6
4. Keycloak Installation & Konfiguration
4. Node 18 & Angular 19 für Frontend

## Quick Start in Visual Studio Code
1. Start der Applikation als Java Applikation

## Deployment
Für das Deployment kann der Befehl 'mvn package' aus dem Hauptverzeichnis des gesamten Projekts 'Rehcnungsapp' verwendet werden. Die Eltern-pom.xml wird die Kompilierung
des Frontends und Backends in der richtigen Reihenfolge auslösen, sowie die kompilierten Datein des frontends in das 'static'-Verzeichnis des Backends kopieren.

Zum Starten der Applikation kann die Start-Batchdatei aus dem Hauptverzeichnis verwendet werden. Dazu kann diese einfach in der CMD aufgerufen werden. 

## Development

Zum Starten des Projekt aus Visual Studio Code heraus, muss das Projekt in der IDE geöffnet werden und als Java Applikation gestartet werden. Ein einfacher Start ist beispielsweise
über das Plugin 'Spring Boot Dashboard' möglich.

## Konfiguration

In der Datei 'application.properties' sind alle Konfigurationen für das Backend zu finden. Diese können bei Bedarf geänert werden (z.B. Port der Applikation).

Einige Konfigurationen müssen angepasst werden, da diese indivduell abhängig sind:
1. 'spring.mail.username' und 'spring.mail.password'
2. 'spring.security.oauth2.client.registration.keycloak.client-secret'

Das Erzeugen der benötigten Credentials wird in der Dokumentation erklärt.
