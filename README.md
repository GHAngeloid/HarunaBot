# HarunaBot
Custom Discord Chat Bot written in Java with [JDA](https://github.com/DV8FromTheWorld/JDA).

## Overview
  - HarunaBot.java
    - Initializes JDA and EventListeners.
  - AppConfig.java
    - Initialize properties and fields from external files.
  - Command.java
    - Object for individual custom commands.
  - JSONReader.java
    - Interprets JSON information from a given URL.
  - CommandListener.java
    - Listens for osu!-related URLs such as profiles and beatmaps.
    - Command List:
      - !commands 
        - Displays all commands.
      - !ping 
        - Checks ping to bot.
      - !roll 
        - Returns a roll from 1-10 or a specified roll.
      - !server 
        - Server stats.
      - !love
        - Determines who the Member loves in the server.
      - !role
        - List of roles.
        - Info on roles.
      - !osu 
        - Top 5 top ranks of specified osu! User 
        - Recent plays made by osu! User.
        - osu! beatmaps from a specified osu! mapper.
        - osu! beatmaps from a specified date.
      - !twitch
        - User can search for Twitch profile and top games livestreamed.
      - !waifu
        - Returns a random picture.
      - !waifudump
        - Returns three random pictures.
      - !activity
        - Activity instance of caller.
        - Activity instance of specified Member.
      - !add
        - User can add a custom command.
        - The command output can be text or an image.
      - !delete
        - User that created a custom command can choose to delete it.
  - StatusListener.java
    - Prints status of bot on Console.
  - GuildMemberListener.java
    - Fired when a User joins the server, changes Status, changes Activity (including Streams), adds/edits/deletes Messages. 
    - If a User connects their Twitch account, they are able to acquire a LIVE role when they go live and climb up the Role Hierarchy. When they end their stream, the role gets removed.
  - VoiceListener.java
    - All voice channel events such as joining/leaving a voice channel and transferring from one channel to another.
  - PMListener.java
    - All private messages to the bot.
  - AuditListener.java
    - Users can view what has been changed on the Discord server such as Text Channels, Voice Channels, Categories, Roles, Emotes, etc.
  
## Web APIs Used:
  - [osu!](https://github.com/ppy/osu-api/wiki)
  - Twitch
  - Danbooru
  

## Notes
I have started building this project since May 2017. This chat bot is to be used under my own Discord server. The application is running as a Maven Project in IntelliJ IDEA (previously in Eclipse until 3/9/18).
