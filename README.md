# HarunaBot
Custom Discord Chat Bot written in Java with [JDA](https://github.com/DV8FromTheWorld/JDA).

## Overview
  - HarunaBot.java
    - Initializes JDA and EventListeners.
  - JSONReader.java
    - Interprets JSON information from given URL.
  - Commands.java
    - Listens for osu!-related URLs such as profiles and beatmaps.
    - Command List:
      - !help - Displays all commands.
      - !ping - Checks ping to bot.
      - !roll - Returns a roll from 1-10 or a specified roll.
      - !emotes - Displays all public emotes on the server.
      - !server - Server stats.
      - !love - Determines who the User loves in the server.
      - !addrole - The User adds a specified role.
      - !deleterole - The User deletes a specified role.
      - !osutopscores - Returns top 5 top ranks of specified osu! User.
      - !game - Returns the game the Author is playing or of a specified User.
      - !pubg - Displays a specified Steam User's recent match in the battle royale game PUBG.
  - StatusListener.java
    - Prints status of bot on Console.
  - GuildMemberListener.java
    - Fired when a User joins the server, changes Status, and changes Game (including Streams).
  - VoiceListener.java
    - All voice channel events such as joining/leaving a voice channel and transferring from one channel to another.
  - PMListener.java
    - All PMs to the bot.
  
## Third-Party APIs Used:
  - [osu! API](https://github.com/ppy/osu-api/wiki)
  - [PUBG API](https://developer.playbattlegrounds.com)

## Notes
I have started building this project since May 2017. This chat bot is to be used under my own Discord server and the Rutgers Esports server. The application is running as a Maven Project in IntelliJ IDEA (previously in Eclipse until 3/9/18).
