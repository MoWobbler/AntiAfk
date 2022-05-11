# AntiAfk

Kick specified players in low tps if they are afk
  
Config options:
  - Minimum_tps: Afk players will only be kicked if tps is less than this value
  - Kick_attempt: This is how often the server will check the list of afk players/ assign players as afk
  - Kick_players: Only players in this list will be kicked for afk, nobody else is affected (Use UUID)


Compiling
-----
Use maven
```
mvn clean package
```
and the built .jar will be in `target/AntiAfk.jar`.
