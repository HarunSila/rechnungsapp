## Frontend

Dieses Projekt ist das Frontend für die Rechnungsapp, ein Projekt, welches im Rahmen der semesterbegleitenden Projektarbeit im Modul Java EE an der FH Südwestfalen erstellt wurde.

Die funktionalen Anforderungen, sowie Archtiektur und Design sind der Aufgabenstellung und Dokumentation zu entnehmen.

# Voraussetzungen
1. VS Code
2. Node 18
3. Angular 19
4. Keycloak Installation & Konfiguration
4. Java 21
5. Maven 3.9.6

# Quick Start in Visual Studio Code
1. npm install
2. ng serve -o

# Deployment
Für das Deployment kann der Befehl 'mvn package' aus dem Hauptverzeichnis des gesamten Projekts 'Rehcnungsapp' verwendet werden. Die Eltern-pom.xml wird die Kompilierung
des Frontends und Backends in der richtigen Reihenfolge auslösen, sowie die kompilierten Datein des frontends in das 'static'-Verzeichnis des Backends kopieren.

Zum Starten der Applikation kann die Start-Batchdatei aus dem Hauptverzeichnis verwendet werden. Dazu kann diese einfach in der CMD aufgerufen werden. 

# Development

Zum Starten des Projekt aus Visual Studio Code heraus, muss das Projekt in der IDE geöffnet werden. Es wird vorrausgesetzt, dass Node in Version 18 und der zugehörige NPM installiert sind.
Die Bibliotheken müssen zunächst über den Befehl 'npm install' installiert werden. Anschließend kann die Applikation via 'ng serve' gestartet werden. 

# Konfiguration

In den Dateien 'environment.ts' und 'environment.development.ts' sind URL zur Backend-Applikation, sowie Keycloak Daten entsprechend einzutragen.
Des Weiteren gehört eine korrekte Realm- und Client-Konfiguration zu einem funktionsfähigen Setup dazu. Die benötigte Keycloak-Konfiguration ist der Dokumentation zu entnehmen.