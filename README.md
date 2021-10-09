# AntiAfk

Not done yet

To do:
  - Basic testing
  - Optimization
  - Prevent players from bypassing with a water pool? (Will look into this later)
  - Log more stuff
  
Config options:
  - Minimum_tps: Afk players will only be kicked if tps is less than this value
  - Afk_seconds: This is how long it takes before a player is identified as afk
  - Online_check_seconds: This is the seconds that needs to pass in order for the online_check function to run. Probably redundant
  - Scheduler_check_seconds: This is how often the server will check the list of afks players/ assign players as afk
  - Kick_players: Only players in this list will be kicked for afk, nobody else is affected (Use UUID)
  - Afk_message: If true, players get a message when they are labeled as afk and not afk
