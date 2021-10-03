# AntiAfk

Not done yet

To do:
  - Basic testing. Right now I get an exception when players are kicked
  - Optimization
  - Prevent players from bypassing with a water pool?
  - Change the timings around
  - Remove "You are now afk" messages
  - Log more stuff
  
Config options:
  - Minimum_tps: Afk players will only be kicked if tps is less than this value
  - Afk_seconds: This is how long it takes before a player is identified as afk
  - Kick_players: Only players in this list will be kicked for afk, nobody else is affected (Use UUID)
