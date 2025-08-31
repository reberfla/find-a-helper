# Find-a-Helper

**Find-a-Helper** ist eine Anwendung, mit der Nutzer Helfer für verschiedene Aufgaben finden und organisieren können.
Das Projekt wurde im Rahmen des Nachdiplomstudiums "Software Engineering" an der ABB Technikerschule Baden erstellt.

## Features

- Suche nach Helfern für unterschiedliche Aufgaben
- Angebote für erstellte aufgaben abgeben
- Angebote für die eigene Aufgabe annehmen/ablehnen
- Nutzerverwaltung und Authentifizierung

## Installation

### Voraussetzungen

<strong>Allgemeine Voraussetzungen:</strong>
* [Docker](https://www.docker.com/)

<strong>Zur Entwicklung:</strong>
* [JDK Amazon Corretto 21 LTS](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html)
- [Nodejs 22 LTS](https://nodejs.org/en)


### Applikation starten

Zum Ausführen der Anwendung ohne Entwicklung:
1. Klone das Repository mit `git clone git@github.com:reberfla/find-a-helper.git`
2. Erstelle eine `.env`-Datei basierend auf der bereitgestellten Vorlage `.env.template`.
3. Starte die Anwendung mit Docker Compose:
```bash
docker compose up
```

### Zur Entwicklung
- npm packages installieren: `cd ui && npm install`
- Entwicklung kotlin webserver: `./.dev/start -b` => startet Datenbank als Container und Kotlin Webserver im dev modus
- Entwicklung vue app: `./.dev/start -f` => startet datenbank und backend als Container, Vue App im dev modus
- nutze die zusätzliche `-r` Flag (z.B. `./.dev/start -f -r`) um den Webserver Container neu zu builden
- Kotlin Webserver formatieren: `./gradlew ktfmtFormat`
- Vue App formatieren: `cd ui && npm run format`

## Autoren

- [@ReniIrinyi](https://github.com/ReniIrinyi)
- [@nuvijt](https://github.com/nuvijt)
- [@reberfla](https://github.com/reberfla)
