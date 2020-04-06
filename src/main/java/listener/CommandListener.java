package listener;

import configuration.AppConfig;
import configuration.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.Color;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity.Timestamps;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutionException;

public class CommandListener extends ListenerAdapter{

    // logger object
    static Logger logger = LoggerFactory.getLogger(CommandListener.class);

    // default commands
    static String[] defaultCommands = {"!ping", "!roll", "!role", "!server", "!love", "!osu", "!activity",
        "!choose", "!twitch", "!waifu", "!waifudump", "!add", "!help"};

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event)
    {

        final String PREFIX = "PREFIX";
        final String OSU = "OSU";
        final String TWITCH = "TWITCH";

        // split Message content apart by whitespace
        String[] command = event.getMessage().getContentRaw().split(" ");

        // avoid bot spam
        if(event.getAuthor().isBot()) {
            return;
        }

        // Check if command does not start with prefix or the website is not osu!
        if(!command[0].startsWith(AppConfig.PROPERTIES.getProperty(PREFIX)) && !command[0].startsWith("https://osu.ppy.sh/")) {
            return;
        }

        // Name : Current Channel Name with the addition of changing it
        if(command[0].equalsIgnoreCase("!name")) {
            if(event.getMember().isOwner()) {
                if (command.length > 1) {
                    if (command[1].equals("-edit")) {
                        String s = "";
                        // retrieve new topic
                        for (int i = 2; i < command.length; i++) {
                            s += command[i];
                        }
                        if(s.isEmpty()) {
                            return;
                        }
                        event.getChannel().getManager().setName(s).queue();
                    }
                } else {
                    event.getChannel().sendMessage("Name of text channel -> " + event.getChannel().getName()).queue();
                }
            }
        }

        // Topic : Discover current topic as well as changing it
        else if(command[0].equalsIgnoreCase("!topic")) {
            if(event.getMember().isOwner()) {
                if (command.length > 1) {
                    if (command[1].equals("-edit")) {
                        String s = "";
                        // retrieve new topic
                        for (int i = 2; i < command.length; i++) {
                            s += command[i] + " ";
                        }
                        event.getChannel().getManager().setTopic(s).queue();
                    }
                } else {
                    if (event.getChannel().getTopic() == null || event.getChannel().getTopic().isEmpty()) {
                        event.getChannel().sendMessage("There is no topic on this channel.").queue();
                    } else {
                        event.getChannel().sendMessage("Today's topic on **" + event.getChannel().getName()
                                + "** -> " + event.getChannel().getTopic()).queue();
                    }
                }
            }
        }

        // TODO: Make sure commands stay in appropriate channel(s). Future plan on making this dynamic
        if(command[0].startsWith(AppConfig.PROPERTIES.getProperty(PREFIX))
                && !(event.getChannel().getName().equalsIgnoreCase("botcommands")
                || event.getChannel().getName().equalsIgnoreCase("bottestinglab"))){
            return;
        }

        // initialize DateTime
        LocalDateTime current = LocalDateTime.now();

        // osu! beatmap listener for info
        // CHANGE : include new URL for beatmaps for new website
        if(command[0].startsWith("https://osu.ppy.sh/b/") || command[0].startsWith("https://osu.ppy.sh/s/")
            || command[0].startsWith("https://osu.ppy.sh/beatmapsets/")) {
            String beatmapId = "";

            if(command[0].charAt(19) == 's') {
                beatmapId = "&s=" + command[0].substring(21);
            }else if(command[0].charAt(19) == 'b' && command[0].charAt(20) == '/') {
                beatmapId = "&b=" + command[0].substring(21);
            }else {
                int index = command[0].indexOf("#osu/");
                // not osu! standard
                if(index == -1) {
                    return;
                }
                index += 5;
                beatmapId = "&b=" + command[0].substring(index);
            }

            //beatmapId = command[0].substring(21);
            String mapLink = "https://osu.ppy.sh/api/get_beatmaps?k=" + AppConfig.PROPERTIES.getProperty(OSU)
                    + beatmapId + "&type=string";
            StringBuffer response;
            try{
                response = JSONHelper.connect(mapLink);
            }catch(IOException e){
                return;
            }

            //Read JSON response and print
            JSONArray beatmapMetadataResponse = new JSONArray(response.toString());
            JSONObject data = beatmapMetadataResponse.getJSONObject(0);

            int mapLength = data.getInt("total_length");
            int minutes = mapLength / 60;
            int seconds = mapLength % 60;

            String status = CommandListenerHelper.osuApprovedReader(data.getInt("approved"));

            String output = "**Song:** " + data.getString("title") + "\n"
                    + "**Artist:** " + data.getString("artist") + "\n"
                    + "**Diff Name:** " + data.getString("version") + "\n"
                    + "**Mapper:** " + data.getString("creator") + "\n"
                    + "**Length:** " + String.format("%d:%02d", minutes, seconds) + "\t**BPM:** " + data.getDouble("bpm") + "\n"
                    + "**Star Difficulty:** " + String.format("%.2f",data.getDouble("difficultyrating")) + "\n"
                    + "**CS:** " + data.getDouble("diff_size") + "\t**AR:** " + data.getDouble("diff_approach")
                    + "\t**HP:** " + data.getDouble("diff_drain") + "\t**OD:** " + data.getDouble("diff_overall") + "\n"
                    + "**Status:** " + status + "\n"
                    + "**Links:**\t[Download](https://osu.ppy.sh/d/" + data.getInt("beatmapset_id")
                    + ")\t[Bloodcat Mirror](http://bloodcat.com/osu/s/" + data.getInt("beatmapset_id") + ")";
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(output);
            eb.setThumbnail("https://b.ppy.sh/thumb/" + data.getInt("beatmapset_id") + "l.jpg");
            eb.setFooter("osu!", "https://osu.ppy.sh/images/layout/osu-logo@2x.png");
            event.getChannel().sendMessage(eb.build()).queue();
        }


        // osu! profile stats
        else if(command[0].startsWith("https://osu.ppy.sh/u/") || command[0].startsWith("https://osu.ppy.sh/users/")) {
            String user = "";

            if(command[0].charAt(20) == '/') {
                user = command[0].substring(21);
            }else {
                user = command[0].substring(25);
            }

            String profileLink = "https://osu.ppy.sh/api/get_user?k=" + AppConfig.PROPERTIES.getProperty(OSU) + "&u=" + user + "&type=id";
            StringBuffer response;
            try{
                response = JSONHelper.connect(profileLink);
            }catch(IOException e){
                return;
            }

            //Read JSON response and print
            JSONArray userResponse = new JSONArray(response.toString());
            JSONObject data = userResponse.getJSONObject(0);

            String output = "**[" + data.getString("username") + "](https://osu.ppy.sh/users/" + data.getString("user_id") + ")**\n"
                    + "Level: " + (int)data.getDouble("level") + "\n"
                    + "Global Rank: " + String.format("%,d", data.getInt("pp_rank"))
                    + " (" + data.getString("country") + " #" + String.format("%,d", data.getInt("pp_country_rank")) + ")\n"
                    + "Total PP: " + String.format("%,.2f", data.getDouble("pp_raw")) + "pp\n"
                    + "Ranked Score: " + String.format("%,d", data.getLong("ranked_score")) + "\n"
                    + "Hit Accuracy: " + String.format("%.2f", data.getDouble("accuracy")) + "%\n"
                    + "Playcount: " + String.format("%,d", data.getInt("playcount"));
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(output);
            eb.setThumbnail("https://a.ppy.sh/" + data.getString("user_id"));
            eb.setFooter("osu!", "https://osu.ppy.sh/images/layout/osu-logo@2x.png");
            event.getChannel().sendMessage(eb.build()).queue();
        }


        // ping
        else if(command[0].equalsIgnoreCase("!ping")) {
            String msg = "Pong! `" + event.getJDA().getGatewayPing() + "ms`";
            if(command.length == 1) {
                event.getChannel().sendMessage(msg).queue();
            }else if(command.length == 2 && command[1].equalsIgnoreCase("-e")) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.ORANGE);
                eb.setDescription(msg);
                event.getChannel().sendMessage(eb.build()).queue();

            }
        }


        // help
        else if(command[0].equalsIgnoreCase("!help")) {
            event.getChannel().sendMessage("Use **!commands** for full list of commands.").queue();
        }


        // commands
        else if(command[0].equalsIgnoreCase("!commands")){
            String output = "";
            boolean isFlag = true;
            if(command.length == 2) {
                if(command[1].equalsIgnoreCase("-custom")) {
                    output = "**Custom Commands**";

                    if(AppConfig.commandSet.size() == 0) {
                        output += "\n<empty>";
                    }
                    else {
                        for(Command currentCommand : AppConfig.commandSet) {
                            output += "\n" + currentCommand.getCommandName();
                        }
                    }
                    isFlag = false;
                }
            }
            if(isFlag) {
                output = "**Default Commands**";
                for(String currentCommand : defaultCommands) {
                    output += '\n' + currentCommand;
                }
                output += "\n\nFor custom commands, please include '-custom' flag. (i.e. **!commands -custom**)";
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(output);
            event.getChannel().sendMessage(eb.build()).queue();

            //need to find a better solution to print all commands through a message
        }


        // roll
        else if(command[0].equalsIgnoreCase("!roll")){
            int max = 0;
            if(command.length == 1) {
                max = 100;
            }else {
                try {
                    max = Integer.parseInt(command[1]);
                    if(max <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    max = 100;
                }
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(event.getAuthor().getName() + " rolls " + CommandListenerHelper.roll(max));
            event.getChannel().sendMessage(eb.build()).queue();
        }//returns a random # from 1 to 100


        // choose
		else if(command[0].equalsIgnoreCase("!choose")) {
			if(command.length == 1) {
                event.getChannel().sendMessage("Choose command. Returns a choice that is given with command. "
                + "(i.e. !choose <choice1>, <choice2>, <choiceN>)").queue();
                return;
			}
			command[0] = "";
			String line = String.join(" ", command).trim();
			String[] options = line.split(",");

            if(options.length == 1) {
                //example: !choose sleep
                event.getChannel().sendMessage("I suggest " + options[0]).queue();
            }
            else if(options.length > 1) {
                //example: !choose code, sleep, play
                int indexPick = CommandListenerHelper.roll(options.length);
                String temp = options[indexPick - 1].trim();
                event.getChannel().sendMessage("I suggest " + temp).queue();
            }
		}// returns the arg that is chosen


        // love
        else if(command[0].equalsIgnoreCase("!love")) {
            int maxMembers = event.getGuild().getMembers().size();
            String lover = "";
            do {
                lover = event.getGuild().getMembers().get(CommandListenerHelper.roll(maxMembers)).getUser().getName();
            }while(event.getAuthor().getName().equals(lover));
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(event.getAuthor().getName() + " loves " + lover + " <3");
            event.getChannel().sendMessage(eb.build()).queue();
        }// returns the user "loving" a random user in guild


        // role
        else if(command[0].equalsIgnoreCase("!role")) {
            if(command.length == 1) {
                event.getChannel().sendMessage("Role Commands. Command flags to use after **!role**: -list -info").queue();
                return;
            }

            // Replaces !rolelist command
            if(command[1].equalsIgnoreCase("-list")) {
                String output = "**List of available roles:**\n";
                // Display roles that are allowed. Make it Permission sensitive!
                Role role;
                boolean isAllowed = true;
                for(int i = 0; i < event.getGuild().getRoles().size(); i++){
                    role = event.getGuild().getRoles().get(i);
                    if(role.getName().equals("@everyone") || role.getName().equals("LIVE")) {
                        continue;
                    }

                    if(role.getPermissions().contains(Permission.ADMINISTRATOR)
                            || role.getPermissions().contains(Permission.KICK_MEMBERS)
                            || role.getPermissions().contains(Permission.BAN_MEMBERS)
                            || role.isManaged()) {
                        isAllowed = false;
                    }
                    if(isAllowed) {
                        output += role.getName() + "\n";
                    }else{
                        isAllowed = true;
                    }
                }
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.ORANGE);
                eb.setDescription(output);
                event.getChannel().sendMessage(eb.build()).queue();
            }
            // Where to get roles?
            else if(command[1].equalsIgnoreCase("-info")) {
                event.getChannel().sendMessage("Assignable roles can be found in **#roles**.").queue();
            }
        }


        // osu
        else if(command[0].equalsIgnoreCase("!osu")) {
            if(command.length == 1) {
                event.getChannel().sendMessage("osu! Commands. Flags to use after **!osu**: -top -recent -maps").queue();
            }
            if(command.length > 1) {
                // Top Scores
                if (command[1].equalsIgnoreCase("-top")) {
                    JSONArray json = null;
                    String user = "";
                    if (command.length < 3) {
                        event.getChannel().sendMessage("Retrieve an osu! player's top scores. (i.e. !osu -top <Username>)").queue();
                        return;
                    }
                    user = command[2];
                    for (int i = 3; i < command.length; i++) {
                        user += " " + command[i];
                    }

                    StringBuffer response = null;
                    try {
                        String site = "https://osu.ppy.sh/api/get_user_best?k=" + AppConfig.PROPERTIES.getProperty(OSU) + "&u=" + user + "&limit=5&type=string";
                        response = JSONHelper.connect(site);
                    } catch (IOException e) {
                        logger.info(e.getMessage());
                        return;
                    }
                    //Read JSON response and print
                    JSONArray userBestResponse = new JSONArray(response.toString());
                    int userId = userBestResponse.getJSONObject(0).getInt("user_id");
                    JSONObject data = null;
                    String title = user;
                    String mapData = "";

                    // iterates through each beatmap to get it's metadata
                    for (int i = 0; i < userBestResponse.length(); i++) {
                        String score = "https://osu.ppy.sh/api/get_beatmaps?k=" + AppConfig.PROPERTIES.getProperty(OSU) + "&b="
                                + userBestResponse.getJSONObject(i).getInt("beatmap_id") + "&type=string";
                        try {
                            response = JSONHelper.connect(score);
                        } catch (IOException e) {
                            logger.info(e.getMessage());
                            return;
                        }
                        //Read JSON response and print
                        JSONArray beatmapMetadataResponse = new JSONArray(response.toString());
                        data = beatmapMetadataResponse.getJSONObject(0);

                        int modCode = userBestResponse.getJSONObject(i).getInt("enabled_mods");
                        String mods = CommandListenerHelper.osuModReader(modCode);
                        mapData += "\n[" + data.getString("artist") + " - " + data.getString("title")
                                + "](https://osu.ppy.sh/beatmapsets/" + data.getString("beatmapset_id") + "#osu/" + data.getString("beatmap_id") + ") ["
                                + data.getString("version") + "]"
                                + " by [" + data.getString("creator") + "](https://osu.ppy.sh/users/" + data.getInt("creator_id") +")\n**"
                                + String.format("%.2f", userBestResponse.getJSONObject(i).getDouble("pp")) + "pp**\n"
                                + "**Mods:** " + mods + "\t" + "**Rank:** " + userBestResponse.getJSONObject(i).getString("rank") + "\n"
                                + "**Date:** " + userBestResponse.getJSONObject(i).getString("date") + " UTC\n";
                    }
                    //https://osu.ppy.sh/images/badges/score-ranks/Score-SS-Small-60@2x.png

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(title, "https://osu.ppy.sh/users/" + userId, "https://a.ppy.sh/" + userId);
                    eb.setColor(Color.ORANGE);
                    eb.setDescription(mapData);
                    eb.setFooter("osu! | Top Scores", "https://osu.ppy.sh/images/layout/osu-logo@2x.png");
                    event.getChannel().sendMessage(eb.build()).queue();
                }
                // Recent Plays
                else if (command[1].equalsIgnoreCase("-recent")) {
                    String user = "";
                    if (command.length < 3) {
                        event.getChannel().sendMessage("Retrieve an osu! player's recent score. (i.e. !osu -recent <Username>)").queue();
                        return;
                    }

                    // get username
                    user = command[2];
                    for (int i = 3; i < command.length; i++) {
                        user += " " + command[i];
                    }

                    // establish connection for user recent scores
                    StringBuffer response = null;
                    try {
                        String site = "https://osu.ppy.sh/api/get_user_recent?k=" + AppConfig.PROPERTIES.getProperty(OSU) + "&u=" + user + "&limit=50&type=string";
                        response = JSONHelper.connect(site);
                    } catch (IOException e) {
                        return;
                    }

                    JSONArray userRecentResponse = new JSONArray(response.toString());

                    // if there are no recent scores
                    if (userRecentResponse.isEmpty()) {
                        event.getChannel().sendMessage(user + " did not set any scores within the past 24 hours.").queue();
                        return;
                    }
                    int userId = userRecentResponse.getJSONObject(0).getInt("user_id");

                    // check for a passed score, ignore failed
                    int recentPlayIndex = -1;
                    for(int i = 0; i < userRecentResponse.length(); i++) {
                        if(userRecentResponse.getJSONObject(i).getString("rank").charAt(0) != 'F') {
                            recentPlayIndex = i;
                            break;
                        }
                    }
                    if(recentPlayIndex == -1) {
                        event.getChannel().sendMessage(user + " did not set any scores within the past 24 hours.").queue();
                        return;
                    }

                    JSONObject data = null;
                    String mapData = "";

                    // score data
                    String scoreData = "https://osu.ppy.sh/api/get_scores?k=" + AppConfig.PROPERTIES.getProperty(OSU) + "&b="
                            + userRecentResponse.getJSONObject(recentPlayIndex).getInt("beatmap_id")
                            + "&u=" + user + "&type=string";
                    try {
                        response = JSONHelper.connect(scoreData);
                    } catch(IOException e) {
                        return;
                    }

                    String ppAmount = "";
                    try {
                        JSONArray scoreDataResponse = new JSONArray(response.toString());
                        for(int i = 0; i < scoreDataResponse.length(); i++) {
                            if(scoreDataResponse.getJSONObject(i).getInt("score") == userRecentResponse.getJSONObject(recentPlayIndex).getInt("score")) {
                                ppAmount = String.format("%,.2f", scoreDataResponse.getJSONObject(i).getFloat("pp"));
                                break;
                            }
                        }
                    }catch(JSONException e) {
                        // TODO: What if pp does not exist???
                    }

                    // beatmap metadata
                    String beatmapData = "https://osu.ppy.sh/api/get_beatmaps?k=" + AppConfig.PROPERTIES.getProperty(OSU) + "&b="
                            + userRecentResponse.getJSONObject(recentPlayIndex).getInt("beatmap_id") + "&type=string";
                    try {
                        response = JSONHelper.connect(beatmapData);
                    } catch (IOException e) {
                        return;
                    }

                    //Read JSON response and print
                    JSONArray beatmapMetadataResponse = new JSONArray(response.toString());
                    data = beatmapMetadataResponse.getJSONObject(0);

                    int modCode = userRecentResponse.getJSONObject(recentPlayIndex).getInt("enabled_mods");
                    String mods = CommandListenerHelper.osuModReader(modCode);
                    mapData = "[" + data.getString("artist") + " - " + data.getString("title") + "](https://osu.ppy.sh/beatmapsets/" + data.getString("beatmapset_id") + "#osu/" + data.getString("beatmap_id")
                            + ") [" + data.getString("version") + "]" + " by [" + data.getString("creator") + "](https://osu.ppy.sh/users/" + data.getString("creator_id") + ")\n";
                    if(!ppAmount.isEmpty()) {
                        mapData += "**" + ppAmount + "pp**\n";
                    }
                    mapData += "\n**Score:** " + String.format("%,d", userRecentResponse.getJSONObject(recentPlayIndex).getInt("score"))
                            + "\t(" + userRecentResponse.getJSONObject(recentPlayIndex).getInt("maxcombo") + "/" + data.getInt("max_combo") + "x)"
                            + "\n**300s:** " + userRecentResponse.getJSONObject(recentPlayIndex).getInt("count300")
                            + "\t**100s:** " + userRecentResponse.getJSONObject(recentPlayIndex).getInt("count100")
                            + "\t**50s:** " + userRecentResponse.getJSONObject(recentPlayIndex).getInt("count50")
                            + "\t**Misses:** " + userRecentResponse.getJSONObject(recentPlayIndex).getInt("countmiss") + "\n"
                            + "**Mods:** " + mods + "\t**Rank:** " + userRecentResponse.getJSONObject(recentPlayIndex).getString("rank") + "\n"
                            + "**Date:** " + userRecentResponse.getJSONObject(recentPlayIndex).getString("date") + " UTC";

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(user, "https://osu.ppy.sh/users/" + userId, "https://a.ppy.sh/" + userId);
                    eb.setColor(Color.ORANGE);
                    eb.setDescription(mapData);
                    eb.setFooter("osu! | Recent Play", "https://osu.ppy.sh/images/layout/osu-logo@2x.png");
                    event.getChannel().sendMessage(eb.build()).queue();

                }
                // beatmap commands
                else if(command[1].equalsIgnoreCase("-maps")) {
                    if(command.length == 2) {
                        event.getChannel().sendMessage("osu! Beatmap Commands. Flags to use after **-maps**: -from -since").queue();
                        return;
                    }
                    // -from : Random beatmap from specified User
                    if(command[2].equalsIgnoreCase("-from")) {
                        if(command.length == 3) {
                            event.getChannel().sendMessage("Retrieve random beatmap from specified User. (i.e. !osu -maps -from <Username>)").queue();
                            return;
                        }
                        // collect User
                        String user = "";
                        user = command[3];
                        for (int i = 4; i < command.length; i++) {
                            user += " " + command[i];
                        }

                        StringBuffer response = null;
                        try {
                            String site = "https://osu.ppy.sh/api/get_beatmaps?k=" + AppConfig.PROPERTIES.getProperty(OSU) + "&u=" + user + "&m=0&type=string";
                            response = JSONHelper.connect(site);
                        } catch (IOException e) {
                            return;
                        }

                        JSONArray beatmapResponse = new JSONArray(response.toString());
                        if(beatmapResponse.isEmpty()) {
                            return;
                        }

                        // select a beatmap to parse metadata
                        Random rd = new Random();
                        int indexOfMap = rd.nextInt(beatmapResponse.length());
                        JSONObject specifiedMap = beatmapResponse.getJSONObject(indexOfMap);

                        // translate beatmap status
                        String status = CommandListenerHelper.osuApprovedReader(specifiedMap.getInt("approved"));

                        // find length
                        int minutes = specifiedMap.getInt("total_length") / 60;
                        int seconds = specifiedMap.getInt("total_length") % 60;

                        // parse metadata
                        String mapData = "";
                        mapData = "[" + specifiedMap.getString("artist") + " - " + specifiedMap.getString("title")
                                + "](https://osu.ppy.sh/beatmapsets/" + specifiedMap.getString("beatmapset_id") + ")\n"
                                + "**Length:** " + String.format("%d:%02d", minutes, seconds)
                                + "\t**BPM:** " + specifiedMap.getInt("bpm") + "\n"
                                + "**Status:** " + status;

                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(user, "https://osu.ppy.sh/users/" + specifiedMap.getInt("creator_id"), "https://a.ppy.sh/" + specifiedMap.getInt("creator_id"));
                        eb.setColor(Color.ORANGE);
                        eb.setDescription(mapData);
                        eb.setFooter("osu!", "https://osu.ppy.sh/images/layout/osu-logo@2x.png");
                        event.getChannel().sendMessage(eb.build()).queue();
                        // TODO: Future expansion, specify "approved" status (i.e. Ranked, Loved, etc.)
                    }
                    else if(command[2].equalsIgnoreCase("-since")) {
                        if(command.length == 3) {
                            event.getChannel().sendMessage("Retrieve beatmaps from specified date. (i.e. !osu -maps -since <MM/DD/YY>)").queue();
                            return;
                        }

                        // date is incorrect
                        if(command[3].length() != 8) {
                            return;
                        }

                        // Example SQL Date : 2013-01-01 00:00:00
                        String sqlFormattedDate = "20" + command[3].substring(6) + "-" + command[3].substring(0, 2)
                                + "-" + command[3].substring(3, 5) + "%2000:00:00";

                        // make connection to get beatmaps
                        StringBuffer response = null;
                        try {
                            String site = "https://osu.ppy.sh/api/get_beatmaps?k=" + AppConfig.PROPERTIES.getProperty(OSU) + "&since=" + sqlFormattedDate +"&limit=50&m=0&type=string";
                            response = JSONHelper.connect(site);
                        } catch (IOException e) {
                            return;
                        }

                        JSONArray beatmapsResponse = new JSONArray(response.toString());

                        // check if date is in the future
                        if(beatmapsResponse.isEmpty()) {
                            event.getChannel().sendMessage("Are you from the future?").queue();
                            return;
                        }

                        String mapData = "";
                        boolean mapExists = false;
                        int beatmapset = 0;
                        for(int i = 0; i < beatmapsResponse.length(); i++) {
                            // check if beatmapset was already recorded and the date is the same
                            if(beatmapsResponse.getJSONObject(i).getInt("beatmapset_id") != beatmapset && beatmapsResponse.getJSONObject(i).getString("approved_date").charAt(9) == command[3].charAt(4)) {
                                mapData += "\n[" + beatmapsResponse.getJSONObject(i).getString("artist") + " - "
                                        + beatmapsResponse.getJSONObject(i).getString("title") + "](https://osu.ppy.sh/beatmapsets/"
                                        + beatmapsResponse.getJSONObject(i).getString("beatmapset_id") + ") by "
                                        + beatmapsResponse.getJSONObject(i).getString("creator") + "\n";
                                beatmapset = beatmapsResponse.getJSONObject(i).getInt("beatmapset_id");
                                mapExists = true;
                            }

                        }

                        // test if date is from the past
                        if(mapExists) {
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setAuthor("Beatmaps from " + command[3]);
                            eb.setColor(Color.ORANGE);
                            eb.setDescription(mapData);
                            eb.setFooter("osu!", "https://osu.ppy.sh/images/layout/osu-logo@2x.png");
                            event.getChannel().sendMessage(eb.build()).queue();
                        }else {
                            event.getChannel().sendMessage("osu! did not exist yet.").queue();
                        }
                    }
                }
            }

        }


        // server
        else if(command[0].equalsIgnoreCase("!server")) {
            int online = 0, offline = 0;
            for(int i = 0; i < event.getGuild().getMembers().size(); i++) {
                if(event.getGuild().getMembers().get(i).getOnlineStatus().name().equals("OFFLINE")) {
                    offline++;
                }else {
                    online++;
                }
            }
            String output = "Population: " + event.getGuild().getMembers().size() + "\n"
                    + "Online: " + online +"\tOffline: " + offline;
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setAuthor(event.getGuild().getName(), null, event.getGuild().getIconUrl());
            eb.setDescription(output);
            eb.setFooter("Created on " + event.getGuild().getTimeCreated().getMonthValue() + "/"
                    + event.getGuild().getTimeCreated().getDayOfMonth() + "/"
                    + event.getGuild().getTimeCreated().getYear()
                    + " | " + current.toString(), null);
            event.getChannel().sendMessage(eb.build()).queue();
        }


        // activity (game)
        else if(command[0].equalsIgnoreCase("!activity")) {
            String user = "";
            EmbedBuilder eb = new EmbedBuilder();

            if(command.length == 1) {
                List<Activity> activities = event.getMember().getActivities();
                if(activities.get(0) != null) {
                    // is RichPresence
                    if(activities.get(0).isRich()) {
                        eb.setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl());
                        eb.setDescription(activities.get(0).asRichPresence().getName() + "\n"
                                + activities.get(0).asRichPresence().getDetails() + "\n"
                                + activities.get(0).asRichPresence().getState() + "\n");
                        eb.setThumbnail(activities.get(0).asRichPresence().getLargeImage().getUrl());
                        eb.setColor(Color.ORANGE);
                        event.getChannel().sendMessage(eb.build()).queue();
                        return;
                    }else {
                        // not RichPresence, possible that it is a Stream
                        System.out.println(activities.get(0).getName() + " " + activities.get(0).getType());
                        eb.setDescription(event.getAuthor().getName() + " is playing " + activities.get(0).getName());
                        eb.setColor(Color.ORANGE);
                        event.getChannel().sendMessage(eb.build()).queue();
                        return;
                    }
                }else {
                    eb.setColor(Color.ORANGE);
                    eb.setDescription("You are not playing a game right now.");
                    event.getChannel().sendMessage(eb.build()).queue();
                    return;
                }
            }else if(command.length > 2) {
                user = command[1];
                for(int i = 2; i < command.length; i++) {
                    user += " " + command[i];
                }
            }else {
                user = command[1];
            }
            if(event.getGuild().getMembersByName(user, true).isEmpty()) {
                return;
            }
            Member member = event.getGuild().getMembersByName(user, true).get(0);
            List<Activity> activities = member.getActivities();
            if(!activities.isEmpty()) {
                // is RichPresence
                if(activities.get(0).isRich()) {
                    String gameName = "";
                    String gameDetails = "";
                    String gameTimestamps = "";
                    try {
                        gameName = activities.get(0).asRichPresence().getName();
                        gameDetails = activities.get(0).asRichPresence().getDetails();
                        Timestamps timestamps = activities.get(0).asRichPresence().getTimestamps();
                        gameTimestamps = "\n" + timestamps.toString();
                    }catch(NullPointerException e) {
                        return;
                    }

                    System.out.println(gameName + "\n"
                            + gameDetails + "\n"
                            + gameTimestamps);
                    eb.setAuthor(member.getUser().getName(), null, member.getUser().getAvatarUrl());
                    eb.setDescription(gameName + "\n" + gameDetails);
                    try{
                        eb.setThumbnail(activities.get(0).asRichPresence().getLargeImage().getUrl());
                    }
                    catch(NullPointerException e) {

                    }
                    eb.setColor(Color.ORANGE);
                    event.getChannel().sendMessage(eb.build()).queue();

                }else {
                    // not RichPresence, possible that it is a Stream
                    //game.getUrl() will always return null if DEFAULT, no STREAMING
                    if(activities.get(0).getType().toString().equals("STREAMING")) {
                        eb.setAuthor(member.getUser().getName() + " is streaming.", null, member.getUser().getAvatarUrl());
                        eb.setDescription(activities.get(0).getName() + "\n" + activities.get(0).getUrl());
                        eb.setColor(Color.ORANGE);
                        event.getChannel().sendMessage(eb.build()).queue();
                    }else {
                        if(activities.get(0).getType().toString().equals("LISTENING")) {
                            eb.setAuthor(member.getUser().getName() + " is listening to " + activities.get(0).getName(), null, member.getUser().getAvatarUrl());
                        }else {
                            eb.setAuthor(member.getUser().getName() + " is playing " + activities.get(0).getName(), null, member.getUser().getAvatarUrl());
                        }
                        eb.setColor(Color.ORANGE);
                        event.getChannel().sendMessage(eb.build()).queue();
                    }
                }
            }else {
                eb.setDescription(member.getUser().getName() + " is not playing a game right now.");
                eb.setColor(Color.ORANGE);
                event.getChannel().sendMessage(eb.build()).queue();
            }
        }

        else if(command[0].equalsIgnoreCase("!twitch")) {
            if(command.length == 1) {
                event.getChannel().sendMessage("Twitch Commands. Flags to use after **!twitch**: -user").queue();
                return;
            }
            if(command[1].equalsIgnoreCase("-user")) {
                if(command.length == 2) {
                    event.getChannel().sendMessage("Twitch User. Returns profile data. (i.e. !twitch -user GHAngeloid)").queue();
                }
                else if(command.length == 3) {
                    try {
                        String link = "https://api.twitch.tv/helix/users?login=" + command[2];
                        StringBuffer response = JSONHelper.connect(link, "Client-ID", AppConfig.PROPERTIES.getProperty(TWITCH));
                        JSONObject profileData = new JSONObject(response.toString()).getJSONArray("data").getJSONObject(0);

                        link = "https://api.twitch.tv/helix/users/follows?to_id=" + profileData.getInt("id") + "&first=1";
                        response = JSONHelper.connect(link, "Client-ID", AppConfig.PROPERTIES.getProperty(TWITCH));
                        int followers = new JSONObject(response.toString()).getInt("total");
                        EmbedBuilder eb = new EmbedBuilder();
                        String broadcasterType = "";
                        if(!profileData.getString("broadcaster_type").isEmpty()) {
                            broadcasterType = profileData.getString("broadcaster_type");
                            broadcasterType = Character.toUpperCase(broadcasterType.charAt(0)) + broadcasterType.substring(1);
                            broadcasterType = "\n\n" + broadcasterType;
                        }

                        eb.setColor(Color.ORANGE);
                        eb.setAuthor(profileData.getString("display_name"), "https://twitch.tv/" + command[2],
                                profileData.getString("profile_image_url"));
                        eb.setDescription(profileData.getString("description")
                                + broadcasterType + "\n"
                                + String.format("%,d", followers) + " followers\n"
                                + String.format("%,d", profileData.getLong("view_count")) + " views");
                        eb.setFooter("Twitch | Profile");
                        event.getChannel().sendMessage(eb.build()).queue();
                    }
                    catch(Exception e) {
                        logger.info("Twitch API lookup failed");
                    }
                }
            }
            else if(command[1].equalsIgnoreCase("-topgames")) {
                try {
                    String link = "https://api.twitch.tv/helix/games/top?first=5";
                    StringBuffer response = JSONHelper.connect(link, "Client-ID", AppConfig.PROPERTIES.getProperty(TWITCH));
                    String description = "";
                    for(int i = 0; i < 5; i++) {
                        JSONObject data = new JSONObject(response.toString()).getJSONArray("data").getJSONObject(i);
                        String gameName = data.getString("name");
                        String gameNameURL = String.join("%20", gameName.split(" "));
                        description += (i + 1) + ". [" + gameName + "](https://twitch.tv/directory/game/" + gameNameURL + ")\n";
                    }
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.ORANGE);
                    eb.setAuthor("Top Games on Twitch");
                    eb.setDescription(description);
                    eb.setFooter("Twitch | Top Games");
                    event.getChannel().sendMessage(eb.build()).queue();
                }
                catch(Exception e) {
                    logger.info("Twitch API lookup failed");
                }
            }
        }

        // pic/gif commands
        else if(command[0].equalsIgnoreCase("!waifu")) {
            try {
                logger.info("waifu command called by " + event.getAuthor().getName());
                boolean isNSFW = true;
                JSONObject image = null;
                String fileURL = "";
                for(int apiLookUp = 0; apiLookUp < 10; apiLookUp++){
                    if(!isNSFW) {
                        // break for-loop
                        break;
                    }
                    String link = "https://danbooru.donmai.us/posts.json?limit=50&random=true&tags=solo%20rating%3As";
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    conn.setRequestProperty("Accept", "application/json");
                    InputStream inputStream = conn.getInputStream();
                    StringBuffer stringBuffer = JSONHelper.toStringBuffer(inputStream);
                    conn.disconnect();

                    JSONArray imageDataArray = new JSONArray(stringBuffer.toString());
                    for(int i = 0; i < imageDataArray.length(); i++) {
                        image = imageDataArray.getJSONObject(i);
                        try {
                            fileURL = image.getString("file_url");
                            if(!fileURL.endsWith(".jpg") && !fileURL.endsWith(".png") && !fileURL.endsWith(".gif")) {
                                throw new JSONException("Video link");
                            }
                        }
                        catch(JSONException e){
                            continue;
                        }
                        if(image.getString("rating").equals("s")
                                && !JSONHelper.isNSFW(image.getString("tag_string"))
                                && image.getInt("score") > 20
                                && !fileURL.isEmpty()) {
                            logger.info(event.getAuthor().getName() + ": https://danbooru.donmai.us/posts/" + image.getInt("id"));
                            isNSFW = false;
                            break;
                        }
                    }
                }
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.ORANGE);
                eb.setAuthor(event.getAuthor().getName() + "'s Waifu", null, event.getAuthor().getAvatarUrl());
                eb.setImage(fileURL);
                eb.setFooter("Waifu");
                event.getChannel().sendMessage(eb.build()).queue();
            }
            catch(Exception e) {
                logger.debug(e.getMessage());
            }
        }

        else if(command[0].equalsIgnoreCase("!waifudump")) {
            try {
                logger.info("waifudump command called by " + event.getAuthor().getName());
                boolean isNSFW = true;
                JSONObject image = null;
                int imageCount = 0;
                ArrayList<String> imageURLArray = new ArrayList<>();
                for(int apiLookUp = 0; apiLookUp < 10; apiLookUp++){
                    if(!isNSFW) {
                        // break for-loop
                        break;
                    }
                    String link = "https://danbooru.donmai.us/posts.json?limit=100&random=true&tags=solo%20rating%3As%20-panties";
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    conn.setRequestProperty("Accept", "application/json");
                    InputStream inputStream = conn.getInputStream();
                    StringBuffer stringBuffer = JSONHelper.toStringBuffer(inputStream);
                    conn.disconnect();

                    JSONArray imageDataArray = new JSONArray(stringBuffer.toString());
                    for(int i = 0; i < imageDataArray.length(); i++) {
                        if(imageCount == 3) {
                            isNSFW = false;
                            break;
                        }
                        image = imageDataArray.getJSONObject(i);
                        String fileURL = "";
                        try {
                            fileURL = image.getString("file_url");
                            if(!fileURL.endsWith(".jpg") && !fileURL.endsWith(".png") && !fileURL.endsWith(".gif")) {
                                throw new JSONException("Video link");
                            }
                        }
                        catch(JSONException e){
                            continue;
                        }
                        if(image.getString("rating").equals("s")
                                && !JSONHelper.isNSFW(image.getString("tag_string"))
                                && image.getInt("score") > 20
                                && !fileURL.isEmpty()) {
                            logger.info(event.getAuthor().getName() + ": https://danbooru.donmai.us/posts/" + image.getInt("id"));
                            imageURLArray.add(fileURL);
                            imageCount += 1;
                        }
                    }
                }
                // print every picture message
                int count = 1;
                for(String imageLink : imageURLArray) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(event.getAuthor().getName() + "'s Waifu", null, event.getAuthor().getAvatarUrl());
                    eb.setColor(Color.ORANGE);
                    eb.setImage(imageLink);
                    eb.setFooter("Waifu Dump (" + count + "/" + imageURLArray.size() + ")");
                    event.getChannel().sendMessage(eb.build()).queue();
                    count += 1;
                }
            }
            catch(Exception e) {
                logger.debug(e.getMessage());
            }
        }

        /*
        else if(command[0].equalsIgnoreCase("!sdvx")) {
            try {
                URL url = new URL("https://sdvx.in/sort/sort_19.htm");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/html");
                InputStream inputStream = conn.getInputStream();
                StringBuffer stringBuffer = JSONHelper.toStringBuffer(inputStream);
                System.out.println(stringBuffer.toString());
                String jsCode = stringBuffer.toString();

                String[] chartData = jsCode.split("<script>");
                for(String chart : chartData) {
                    System.out.println(chart);
                }
                conn.disconnect();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        */

        // add custom command
        else if(command[0].equalsIgnoreCase("!add")) {
            boolean notEligible = true;
            // check if Role is eligible for command
            for(Role role : event.getMember().getRoles()) {
                if(role.getName().equalsIgnoreCase("Nitro Booster") || role.getName().equalsIgnoreCase("Nugget Gang") || role.getName().equalsIgnoreCase("Mods")) {
                    notEligible = false;
                }
            }
            if(notEligible) {
                logger.info("User cannot write new custom command");
                return;
            }
            if(command.length < 2) {
                event.getChannel().sendMessage("Add custom command. Use format: **!add <command name> <task>** (i.e. **!add test Kappa123**)").queue();
                return;
            }

            // command and attachment
            else if(command.length == 2 && command[1].matches("^[a-zA-Z]+$") && event.getMessage().getAttachments().size() > 0) {
                try {
                    Message.Attachment attachment = event.getMessage().getAttachments().get(0);
                    Command newCommand = new Command("!" + command[1], attachment.retrieveInputStream().get(), attachment.getFileName(), event.getAuthor().getIdLong());
                    boolean isAdded = AppConfig.commandSet.add(newCommand);
                    if(isAdded) {
                        FileOutputStream fileOutputStream = new FileOutputStream(AppConfig.PROPERTIES.getProperty("SERIALFILE"));
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(AppConfig.commandSet);
                        objectOutputStream.close();
                        fileOutputStream.close();
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(Color.GREEN);
                        eb.setDescription("**!" + command[1] + "** command has been added.");
                        eb.setFooter(current.toString());
                        event.getChannel().sendMessage(eb.build()).queue();
                        logger.info("Command add successful");
                    }
                    else {
                        logger.info("Command add failure");
                    }
                } catch (IOException | InterruptedException | ExecutionException e) {
                    logger.info("Command add failure");
                }
            }
            // for commands (!add test Kappa123)
            else if(command[1].matches("^[a-zA-Z]+$") && !command[2].startsWith("!") && event.getMessage().getAttachments().size() == 0) {
                String task = command[2];
                for(int i = 3; i < command.length; i++) {
                    task += " " + command[i];
                }
                for(String currentCommand : defaultCommands) {
                    // check if new command matches default commands
                    if(currentCommand.equalsIgnoreCase("!" + command[1])) {
                        logger.info("Command exists as default");
                        return;
                    }
                }
                try {
                    Command newCommand = new Command("!" + command[1], task, event.getAuthor().getIdLong());
                    boolean isAdded = AppConfig.commandSet.add(newCommand);
                    if(isAdded) {
                        FileOutputStream fileOutputStream = new FileOutputStream(AppConfig.PROPERTIES.getProperty("SERIALFILE"));
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(AppConfig.commandSet);
                        objectOutputStream.close();
                        fileOutputStream.close();
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(Color.GREEN);
                        eb.setDescription("**!" + command[1] + "** command has been added.");
                        eb.setFooter(current.toString());
                        event.getChannel().sendMessage(eb.build()).queue();
                        logger.info("Command add successful");
                    }
                    else {
                        logger.info("Command add failure");
                    }
                } catch (IOException e) {
                    logger.info("Command add failure");
                }
            }
        }

        // delete custom command (only owner)
        else if(command[0].equalsIgnoreCase("!delete")) {
            if(command.length == 2) {
                boolean status = false;
                for(Command currentCommand : AppConfig.commandSet) {
                    if(currentCommand.getCommandName().equals("!" + command[1])) {
                        // if user matches owner, remove command
                        if(currentCommand.getCommandOwner() == event.getAuthor().getIdLong() || event.getMember().isOwner()) {
                            // remove command from commandSet
                            status = AppConfig.commandSet.remove(currentCommand);
                            break;
                        }
                    }
                }
                if(status) {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(AppConfig.PROPERTIES.getProperty("SERIALFILE"));
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(AppConfig.commandSet);
                        objectOutputStream.close();
                        fileOutputStream.close();
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(Color.RED);
                        eb.setDescription("**!" + command[1] + "** command has been deleted.");
                        eb.setFooter(current.toString());
                        event.getChannel().sendMessage(eb.build()).queue();
                        logger.info("Command delete successful");

                    } catch (IOException e) {
                        logger.info("Command delete failure");
                    }
                }
                else {
                    logger.info("Command to delete does not exist");
                }
            }
        }

        // edit command name or contents
        else if(command[0].equalsIgnoreCase("!edit")) {
            if(command.length >= 3) {
                if(command[1].equalsIgnoreCase("-name")) {
                    if(command.length == 4 && command[3].matches("^[a-zA-Z]+$")) {
                        // i.e. !edit -name test testOne
                        Command temp = null;
                        for (Command currentCommand : AppConfig.commandSet) {
                            if(currentCommand.getCommandName().equalsIgnoreCase("!" + command[2])) {
                                if(currentCommand.getCommandOwner() == event.getAuthor().getIdLong() || event.getMember().isOwner()) {
                                    temp = currentCommand;
                                    AppConfig.commandSet.remove(currentCommand);
                                    break;
                                }
                            }
                        }
                        if(temp != null) {
                            try {
                                temp.setCommandName("!" + command[3]);
                                boolean isAdded = AppConfig.commandSet.add(temp);
                                if (isAdded) {
                                    FileOutputStream fileOutputStream = new FileOutputStream(AppConfig.PROPERTIES.getProperty("SERIALFILE"));
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                    objectOutputStream.writeObject(AppConfig.commandSet);
                                    objectOutputStream.close();
                                    fileOutputStream.close();
                                    EmbedBuilder eb = new EmbedBuilder();
                                    eb.setColor(Color.YELLOW);
                                    eb.setDescription("**!" + command[2] + "** command has changed to **!" + command[3] + "**.");
                                    eb.setFooter(current.toString());
                                    event.getChannel().sendMessage(eb.build()).queue();
                                    logger.info("Command name edit successful");
                                } else {
                                    logger.info("Command name edit failure");
                                }
                            }
                            catch(IOException e) {
                                logger.info("Command name edit failure");
                            }
                        }
                    }
                }
                else if(command[1].equalsIgnoreCase("-content")) {
                    if(command.length == 3 && event.getMessage().getAttachments().size() > 0) {
                        boolean isRemoved = false;
                        Message.Attachment attachment = event.getMessage().getAttachments().get(0);
                        for (Command currentCommand : AppConfig.commandSet) {
                            if(currentCommand.getCommandName().equalsIgnoreCase("!" + command[2])) {
                                if(currentCommand.getCommandOwner() == event.getAuthor().getIdLong() || event.getMember().isOwner()) {
                                    isRemoved = AppConfig.commandSet.remove(currentCommand);
                                    break;
                                }

                            }
                        }
                        // remove command in set
                        if(isRemoved) {
                            try {
                                Command newCommand = new Command("!" + command[2], attachment.retrieveInputStream().get(), attachment.getFileName(), event.getAuthor().getIdLong());
                                boolean isAdded = AppConfig.commandSet.add(newCommand);
                                if(isAdded) {
                                    FileOutputStream fileOutputStream = new FileOutputStream(AppConfig.PROPERTIES.getProperty("SERIALFILE"));
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                    objectOutputStream.writeObject(AppConfig.commandSet);
                                    objectOutputStream.close();
                                    fileOutputStream.close();
                                    EmbedBuilder eb = new EmbedBuilder();
                                    eb.setColor(Color.YELLOW);
                                    eb.setDescription("**!" + command[2] + "** command has been edited.");
                                    eb.setFooter(current.toString());
                                    event.getChannel().sendMessage(eb.build()).queue();
                                    logger.info("Command edit successful");
                                }
                                else {
                                    logger.info("Command edit failure");
                                }
                            }
                            catch(IOException | InterruptedException | ExecutionException e) {
                                logger.info("Command edit failure");
                            }
                        }
                    }
                    else if(command.length > 3 && event.getMessage().getAttachments().size() == 0) {
                        String task = command[3];
                        for(int i = 4; i < command.length; i++) {
                            task += " " + command[i];
                        }
                        boolean isRemoved =false;
                        for(Command currentCommand : AppConfig.commandSet) {
                            if(currentCommand.getCommandName().equalsIgnoreCase("!" + command[2])) {
                                if(currentCommand.getCommandOwner() == event.getAuthor().getIdLong() || event.getMember().isOwner()) {
                                    isRemoved = AppConfig.commandSet.remove(currentCommand);
                                    break;
                                }
                            }
                        }
                        // remove command in set
                        if(isRemoved) {
                            try {
                                Command newCommand = new Command("!" + command[2], task, event.getAuthor().getIdLong());
                                boolean isAdded = AppConfig.commandSet.add(newCommand);
                                if(isAdded) {
                                    FileOutputStream fileOutputStream = new FileOutputStream(AppConfig.PROPERTIES.getProperty("SERIALFILE"));
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                    objectOutputStream.writeObject(AppConfig.commandSet);
                                    objectOutputStream.close();
                                    fileOutputStream.close();
                                    EmbedBuilder eb = new EmbedBuilder();
                                    eb.setColor(Color.YELLOW);
                                    eb.setDescription("**!" + command[2] + "** command has been edited.");
                                    eb.setFooter(current.toString());
                                    event.getChannel().sendMessage(eb.build()).queue();
                                    logger.info("Command edit successful");
                                }
                                else {
                                    logger.info("Command edit failure");
                                }
                            } catch (IOException e) {
                                logger.info("Command edit failure");
                            }
                        }
                    }
                }
            }
        }

        else {
            // custom commands
            AppConfig.commandSet.forEach((currentCommand) -> {
                if(command[0].equalsIgnoreCase(currentCommand.getCommandName())) {
                    if(!currentCommand.getTask().isEmpty()) {
                        event.getChannel().sendMessage(currentCommand.getTask()).queue();
                    }
                    else {
                        event.getChannel().sendFile(currentCommand.getCommandContent(), currentCommand.getFileName()).queue();
                    }

                }
            });
        }

    }// end of commands

}
