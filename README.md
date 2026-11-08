# Client-Server Minesweeper

A minesweeper app that uses client-server architecture with single-player and multiplayer game modes, deployed on an Ubuntu VPS behind HTTPS. Built with JavaFX, Spring Boot and MySQL.

## Screenshots of UI and Gameplay

![Main menu UI](docs/images/main_menu.png) 
*Main Menu*

![Board Screen](docs/images/board_screen.png)
*Board Screen*

![Gameplay](docs/images/gameplay.gif)
*Gameplay for Online Match*

## Features

- Three difficulty choices
- Ability to see personal and top times for each difficulty
- Login and register
- Online race mode: matchmaking, same board for both players and disconnect handling
- Ability to see the total statistics for online games
- Synchronized countdown before matches
- Hosted online server

## Tech Used

- Java 17
- Spring Boot
- MySQL with JdbcClient
- JavaFX
- Jackson
- BCrypt
- Maven
- AtlantaFX
- Caddy
- systemd

## Architecture and Design Points

- Authoritative server
  - Client only sends request and draws states
- Token authentication
  - Every request is authenticated with bearer token, users cannot access other users info
- Boards are same for race game mode by creating them with the same seed
- Thread safe matchmaking
  - Doesn't let a user be stuck in a non-defined state
  - By using locks on queue and each match in order to prevent race conditions
- Scheduled cleanup of non-responsive players

## Getting Started

### Requirements
  - Java 17
  - MySQL
  - Maven

### Playing Online Steps

Install the client and run it. It connects to the server by default.

### Playing Offline Steps

1. Start MySql
2. Create the local properties file with your DB password
3. Start the server
4. Point the client to the local server by changing client properties to *"http://localhost:8080/"*
5. Start the client

## Configuration

- Client
  - server.url --- Clients connection point to server
- Server
  - Environment Variables
    1. DB_URL → Database url for the server
    2. DB_USER → Database username
    3. DB_PASSWORD → Database's password
    4. SERVER_ADDRESS → Default's to 127.0.0.1 so that server only listens to local connections

## Deployment

- Server runs on an Ubuntu VPS
- Spring Boot App runs as a systemd service under a restricted system user
- Caddy acts as a reverse proxy
- MySql runs locally on the server
- Secrets live in the server
- Firewall only allows SSH, HTTP, HTTPS

## Credits

- [AtlantaFX](https://github.com/mkpaz/atlantafx) by mkpaz - UI Theme - MIT License
- [OpenJFX](https://openjfx.io) Desktop Client UI - GPL v2 with Classpath Exception
- [Spring Boot](https://spring.io/projects/spring-boot) Rest API Server - Apache 2.0
- [Jackson](https://github.com/FasterXML/jackson) JSON Serialization - Apache 2.0
- [jBCrypt](https://www.mindrot.org/projects/jBCrypt/) Password Hashing - ISC
- [MySQL Connector/J](https://dev.mysql.com/doc/connector-j/en/) Database Driver - GPL v2 with Universal FOSS Exception

## Roadmap

### Possible Improvements

- Better matchmaking system with rank system
- Using WebSockets instead of polling
- Inviting a specific friend to match
- Match history

### Next Features

- Hosting the server online  --- *Done*
- Countdown before each match start --- *Done*
- Installers for major OS's
- Fixing the UI of EndGameScene --- *Done*
  - Having two different scenes for games and matches